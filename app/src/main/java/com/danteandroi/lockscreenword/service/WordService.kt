package com.danteandroi.lockscreenword.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.blankj.utilcode.util.ToastUtils
import com.danteandroi.lockscreenword.MainActivity
import com.danteandroi.lockscreenword.R
import com.danteandroi.lockscreenword.data.Word
import com.danteandroi.lockscreenword.source.WordsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val WORDS_KEY = "words_key"
private const val CHANNEL_ID = "word_channel"
private const val NOTIFICATION_ID = 1

private const val ACTION_NEXT = "next"
private const val ACTION_DELETE = "delete"
private const val ACTION_FAVORITE = "favorite"

@DelicateCoroutinesApi
@AndroidEntryPoint
class WordService : Service() {

    @Inject
    lateinit var wordsViewModel: WordsViewModel

    private lateinit var remoteViews: RemoteViews
    private lateinit var remoteViewsBig: RemoteViews
    private lateinit var notification: Notification
    private var job: Job? = null
    private var words: List<Word>? = null
    private var word: Word? = null

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_DELETE -> {
                    deleteWord()
                }
                ACTION_FAVORITE -> {
                    favoriteWord()
                }
                ACTION_NEXT -> {
                    nextWord()
                }
            }
        }
    }

    private fun nextWord() {
        randomWord()
    }

    private fun favoriteWord() {
        word?.favorite = true
        GlobalScope.launch {
            wordsViewModel.updateWord(word)
        }
        updateNotification(word)
    }

    private fun deleteWord() {
        word?.deleted = true
        GlobalScope.launch {
            wordsViewModel.updateWord(word)
        }
        randomWord()
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(broadcastReceiver, IntentFilter().apply {
            addAction(ACTION_NEXT)
            addAction(ACTION_DELETE)
            addAction(ACTION_FAVORITE)
        })

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(applicationContext, MainActivity::class.java), flags
        )
        createChannel()
        createNotification("test data", pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.getStringExtra(WORDS_KEY)
        Timber.d("onStartCommand: $data")
        subscribeUi()
        return START_STICKY_COMPATIBILITY
    }

    private fun subscribeUi() {
        job = GlobalScope.launch {
            wordsViewModel.allWords().collectLatest {
                words = it
                Timber.d("allWords: ${it.size}")
            }
        }
        randomWord()
    }

    private fun randomWord() {
        word = words?.randomOrNull()
        while (word != null && word!!.deleted) {
            word = words?.randomOrNull()
        }
        updateNotification(word)
    }

    private fun updateNotification(random: Word?) {
        if (random == null) {
            return
        } else {
            remoteViews.run {
                setTextViewText(R.id.word, random.word)
                setTextViewText(R.id.meaning, random.meaning)
                setImageViewResource(
                    R.id.favorite,
                    if (random.favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
            }
            remoteViewsBig.run {
                setTextViewText(R.id.word, random.word)
                setTextViewText(R.id.meaning, random.meaning)
                setImageViewResource(
                    R.id.favorite,
                    if (random.favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
            }
            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotification(
        data: String,
        pendingIntent: PendingIntent
    ): Notification {
        createRemoteView()
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(data)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setCustomBigContentView(remoteViewsBig)
            .setCustomContentView(remoteViews)
            .build()
        return notification
    }

    private fun createRemoteView() {
        remoteViews = RemoteViews(packageName, R.layout.word_service_notification_small).apply {
            setOnClickPendingIntent(R.id.delete, getPendingIntent(ACTION_DELETE))
            setOnClickPendingIntent(R.id.next, getPendingIntent(ACTION_NEXT))
            setOnClickPendingIntent(R.id.favorite, getPendingIntent(ACTION_FAVORITE))
        }
        remoteViewsBig = RemoteViews(packageName, R.layout.word_service_notification_big).apply {
            setOnClickPendingIntent(R.id.delete, getPendingIntent(ACTION_DELETE))
            setOnClickPendingIntent(R.id.next, getPendingIntent(ACTION_NEXT))
            setOnClickPendingIntent(R.id.favorite, getPendingIntent(ACTION_FAVORITE))
        }
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else PendingIntent.FLAG_UPDATE_CURRENT

        return PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(action),
            flags
        )
    }

    private fun createChannel() {
        val notificationChannel =
            NotificationChannelCompat.Builder(
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .setName(getString(R.string.word_channel_name))
                .build()
        NotificationManagerCompat.from(applicationContext)
            .createNotificationChannel(notificationChannel)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
