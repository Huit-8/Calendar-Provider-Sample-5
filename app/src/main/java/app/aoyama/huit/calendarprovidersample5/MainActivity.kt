package app.aoyama.huit.calendarprovidersample5

import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Events
import androidx.appcompat.app.AppCompatActivity
import app.aoyama.huit.calendarprovidersample5.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            searchCalenderId()
        }

        binding.button2.setOnClickListener {
            // TODO searchCalenderIdで確認したIDに書き換えてください！
            getCalenderEventAll(6)
        }
    }

    // カレンダーID一覧をTextViewに表示する
    // アプリで使うなら、表示までやらずに、データだけ返すメソッドにするのが良きです
    fun searchCalenderId() {
        // 取得したい情報をlistにまとめて入れる
        // 今回はカレンダーの名前を見て、CalenderId、念の為カレンダーのオーナーがわかれば良いので、その3つを取る
        val CALENDAR_PROJECTION = arrayOf(
            Calendars._ID,
            Calendars.NAME,
            Calendars.OWNER_ACCOUNT
        )

        // CALENDAR_PROJECTION配列の要素順で番号を決める
        val CALENDAR_PROJECTION_IDX_ID = 0
        val CALENDAR_PROJECTION_IDX_NAME = 1
        val CALENDAR_PROJECTION_IDX_OWNER_ACCOUNT = 2

        // クエリ条件を設定する
        val uri = Calendars.CONTENT_URI
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        val sortOrder: String? = null

        // クエリを発行してカーソルを取得する
        val cr = contentResolver
        val cur = cr.query(uri, CALENDAR_PROJECTION, selection, selectionArgs, sortOrder)

        var textStr = "id, カレンダー名, ownerAccount\n"
        while (cur?.moveToNext() == true) {
            // カーソルから各プロパティを取得する
            val id = cur.getLong(CALENDAR_PROJECTION_IDX_ID)
            val name = cur.getString(CALENDAR_PROJECTION_IDX_NAME)
            val ownerAccount = cur.getString(CALENDAR_PROJECTION_IDX_OWNER_ACCOUNT)
            textStr += "$id, "
            textStr += "$name, "
            textStr += "$ownerAccount\n"

            println("id $id")
            println("name $name")
            println("ownerAccount $ownerAccount")
        }
        binding.textView.text = textStr
    }

    // 指定したカレンダーIDのイベントをまとめて取得するやつ
    // どのくらいの範囲で取得してるのかは現状調べてない
    fun getCalenderEventAll(targetCalendarId: Int) {
        // 取得したい情報をlistにまとめて入れる
        // たぶんこれだけあれば足りる気がする・・・？
        val EVENT_PROJECTION = arrayOf(
            BaseColumns._ID,
            Events.TITLE,
            Events.DESCRIPTION,
            Events.EVENT_LOCATION,
            Events.EVENT_COLOR,
            Events.DISPLAY_COLOR,
            Events.DTSTART,
            Events.DTEND,
            Events.DURATION,
            Events.EVENT_TIMEZONE,
            Events.EVENT_END_TIMEZONE,
            Events.ALL_DAY,
            Events.RRULE,
            Events.RDATE,
            Events.GUESTS_CAN_MODIFY,
            Events.GUESTS_CAN_INVITE_OTHERS,
            Events.GUESTS_CAN_SEE_GUESTS,
            Events.ORGANIZER,
            Events.CALENDAR_ID,
        )

        // EVENT_PROJECTIONの順番と一致するID、要素数書き換えたり、順番変えたときには、ここも変える必要あり
        val EVENT_PROJECTION_IDX_EVENT_ID = 0
        val EVENT_PROJECTION_IDX_TITLE = 1
        val EVENT_PROJECTION_IDX_DESCRIPTION = 2
        val EVENT_PROJECTION_IDX_EVENT_LOCATION = 3
        val EVENT_PROJECTION_IDX_EVENT_COLOR = 4
        val EVENT_PROJECTION_IDX_DISPLAY_COLOR = 5
        val EVENT_PROJECTION_IDX_DTSTART = 6
        val EVENT_PROJECTION_IDX_DTEND = 7
        val EVENT_PROJECTION_IDX_DURATION = 8
        val EVENT_PROJECTION_IDX_EVENT_TIMEZONE = 9
        val EVENT_PROJECTION_IDX_EVENT_END_TIMEZONE = 10
        val EVENT_PROJECTION_IDX_ALL_DAY = 11
        val EVENT_PROJECTION_IDX_RRULE = 12
        val EVENT_PROJECTION_IDX_RDATE = 13
        val EVENT_PROJECTION_IDX_GUESTS_CAN_MODIFY = 14
        val EVENT_PROJECTION_IDX_GUESTS_CAN_INVITE_OTHERS = 15
        val EVENT_PROJECTION_IDX_GUESTS_CAN_SEE_GUESTS = 16
        val EVENT_PROJECTION_IDX_ORGANIZER = 17
        val EVENT_PROJECTION_IDX_CALENDAR_ID = 18

        val uri = Events.CONTENT_URI
        // クエリっぽい
        val selection = "(" + Events.CALENDAR_ID + " = ?)"
        val selectionArgs = arrayOf(targetCalendarId.toString())

        val cr = contentResolver
        // sortOrder書き換えれば、順番を指定できそう
        val cur: Cursor? = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)

        var textStr = "eventId, イベントタイトル,開始時間,終了時間, 詳細\n"
        while (cur?.moveToNext() == true) {
            // カーソルから各プロパティを取得する
            val eventId: Long = cur.getLong(EVENT_PROJECTION_IDX_EVENT_ID)
            val title: String = cur.getString(EVENT_PROJECTION_IDX_TITLE)
            val description: String? = cur.getString(EVENT_PROJECTION_IDX_DESCRIPTION)
            val eventLocation: String? = cur.getString(EVENT_PROJECTION_IDX_EVENT_LOCATION)
            val eventColdr: Int = cur.getInt(EVENT_PROJECTION_IDX_EVENT_COLOR)
            val displayColor: Int = cur.getInt(EVENT_PROJECTION_IDX_DISPLAY_COLOR)
            val dtStart: Long = cur.getLong(EVENT_PROJECTION_IDX_DTSTART)
            val dtEnd: Long = cur.getLong(EVENT_PROJECTION_IDX_DTEND)
            val duration: String? = cur.getString(EVENT_PROJECTION_IDX_DURATION)
            val eventTimeZone: String? = cur.getString(EVENT_PROJECTION_IDX_EVENT_TIMEZONE)
            val eventEndTimeZone: String? = cur.getString(EVENT_PROJECTION_IDX_EVENT_END_TIMEZONE)
            val allDay: Int = cur.getInt(EVENT_PROJECTION_IDX_ALL_DAY)
            val rRule: String? = cur.getString(EVENT_PROJECTION_IDX_RRULE)
            val rDate: String? = cur.getString(EVENT_PROJECTION_IDX_RDATE)
            val guestsCanModify: Int = cur.getInt(EVENT_PROJECTION_IDX_GUESTS_CAN_MODIFY)
            val guestsCanInviteOthers: Int =
                cur.getInt(EVENT_PROJECTION_IDX_GUESTS_CAN_INVITE_OTHERS)
            val guestCanSeeGuests: Int = cur.getInt(EVENT_PROJECTION_IDX_GUESTS_CAN_SEE_GUESTS)
            val organizer: String? = cur.getString(EVENT_PROJECTION_IDX_ORGANIZER)
            val calenderId: Long = cur.getLong(EVENT_PROJECTION_IDX_CALENDAR_ID)

            // タイトルが無いものは、表示しない
            if (title.isNotEmpty()) {
                textStr += "$eventId, "
                textStr += "$title, "
                textStr += "$dtStart,"
                textStr += "$dtEnd,"
                textStr += "$description\n"
            }
        }
        binding.textView.text = textStr
    }

    // 端末内に入ってるカレンダー情報をすべて取得する動作確認用メソッド
    // イベントを取得しているわけではない
    // ここで取得した、オーナーや、アカウントネーム、アカウントタイプを使って、カレンダーイベントを取る
    fun getCalenderListTest() {
        // プロジェクション配列。
        // 取得したいプロパティの一覧を指定する。
        val CALENDAR_PROJECTION = arrayOf(
            Calendars._ID,
            Calendars.NAME,
            Calendars.ACCOUNT_NAME,
            Calendars.ACCOUNT_TYPE,
            Calendars.CALENDAR_COLOR,
            Calendars.CALENDAR_COLOR_KEY,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.CALENDAR_ACCESS_LEVEL,
            Calendars.CALENDAR_TIME_ZONE,
            Calendars.VISIBLE,
            Calendars.SYNC_EVENTS,
            Calendars.OWNER_ACCOUNT
        )


        val CALENDAR_PROJECTION_IDX_ID = 0
        val CALENDAR_PROJECTION_IDX_NAME = 1
        val CALENDAR_PROJECTION_IDX_ACCOUNT_NAME = 2
        val CALENDAR_PROJECTION_IDX_ACCOUNT_TYPE = 3
        val CALENDAR_PROJECTION_IDX_CALENDAR_COLOR = 4
        val CALENDAR_PROJECTION_IDX_CALENDAR_COLOR_KEY = 5
        val CALENDAR_PROJECTION_IDX_CALENDAR_DISPLAY_NAME = 6
        val CALENDAR_PROJECTION_IDX_CALENDAR_ACCESS_LEVEL = 7
        val CALENDAR_PROJECTION_IDX_CALENDAR_TIME_ZONE = 8
        val CALENDAR_PROJECTION_IDX_VISIBLE = 9
        val CALENDAR_PROJECTION_IDX_SYNC_EVENTS = 10
        val CALENDAR_PROJECTION_IDX_OWNER_ACCOUNT = 11

        // クエリ条件を設定する
        val uri = Calendars.CONTENT_URI
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        val sortOrder: String? = null

        // クエリを発行してカーソルを取得する
        val cr = contentResolver
        val cur = cr.query(uri, CALENDAR_PROJECTION, selection, selectionArgs, sortOrder)

        while (cur?.moveToNext() == true) {
            // カーソルから各プロパティを取得する
            val id = cur.getLong(CALENDAR_PROJECTION_IDX_ID)
            val name = cur.getString(CALENDAR_PROJECTION_IDX_NAME)
            val accountName = cur.getString(CALENDAR_PROJECTION_IDX_ACCOUNT_NAME)
            val accountType = cur.getString(CALENDAR_PROJECTION_IDX_ACCOUNT_TYPE)
            val calendarColor = cur.getInt(CALENDAR_PROJECTION_IDX_CALENDAR_COLOR)
            val calendarColorkey = cur.getString(CALENDAR_PROJECTION_IDX_CALENDAR_COLOR_KEY)
            val calendarDisplayName =
                cur.getString(CALENDAR_PROJECTION_IDX_CALENDAR_DISPLAY_NAME)
            val calendarAccessLevel =
                cur.getInt(CALENDAR_PROJECTION_IDX_CALENDAR_ACCESS_LEVEL)
            val calendarTimeZone = cur.getString(CALENDAR_PROJECTION_IDX_CALENDAR_TIME_ZONE)
            val visible = cur.getInt(CALENDAR_PROJECTION_IDX_VISIBLE)
            val syncEvents = cur.getInt(CALENDAR_PROJECTION_IDX_SYNC_EVENTS)
            val ownerAccount = cur.getString(CALENDAR_PROJECTION_IDX_OWNER_ACCOUNT)
            println("id $id")
            println("name $name")
            println("accountName $accountName")
            println("accountType $accountType")
            println("calendarColor $calendarColor")
            println("calendarColorkey $calendarColorkey")
            println("calendarDisplayName $calendarDisplayName")
            println("calendarAccessLevel $calendarAccessLevel")
            println("calendarTimeZone $calendarTimeZone")
            println("visible $visible")
            println("syncEvents $syncEvents")
            println("ownerAccount $ownerAccount")
        }
    }
}