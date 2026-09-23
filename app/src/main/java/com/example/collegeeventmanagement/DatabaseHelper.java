package com.example.collegeeventmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // =====================================================
    // DATABASE
    // =====================================================

    private static final String DATABASE_NAME =
            "CollegeEventManagement.db";

    private static final int DATABASE_VERSION = 4;


    // =====================================================
    // USERS TABLE
    // =====================================================

    private static final String TABLE_USERS =
            "users";

    private static final String COLUMN_ID =
            "id";

    private static final String COLUMN_NAME =
            "name";

    private static final String COLUMN_EMAIL =
            "email";

    private static final String COLUMN_PASSWORD =
            "password";

    private static final String COLUMN_ROLE =
            "role";


    // =====================================================
    // EVENTS TABLE
    // =====================================================

    private static final String TABLE_EVENTS =
            "events";

    private static final String EVENT_ID =
            "event_id";

    private static final String EVENT_NAME =
            "event_name";

    private static final String EVENT_DESCRIPTION =
            "description";

    private static final String EVENT_DATE =
            "event_date";

    private static final String EVENT_TIME =
            "event_time";

    private static final String EVENT_VENUE =
            "venue";

    private static final String EVENT_ORGANIZER =
            "organizer";

    private static final String EVENT_MAX_REGISTRATIONS =
            "max_registrations";


    // =====================================================
    // REGISTRATIONS TABLE
    // =====================================================

    private static final String TABLE_REGISTRATIONS =
            "registrations";

    private static final String REGISTRATION_ID =
            "registration_id";

    private static final String REG_EVENT_ID =
            "event_id";

    private static final String REG_STUDENT_EMAIL =
            "student_email";

    private static final String REG_STUDENT_NAME =
            "student_name";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // =====================================================
    // CREATE DATABASE
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // -------------------------------------------------
        // USERS TABLE
        // -------------------------------------------------

        String createUsersTable =
                "CREATE TABLE " + TABLE_USERS + " (" +

                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_EMAIL +
                        " TEXT UNIQUE NOT NULL, " +

                        COLUMN_PASSWORD +
                        " TEXT NOT NULL, " +

                        COLUMN_ROLE +
                        " TEXT NOT NULL" +

                        ")";

        db.execSQL(createUsersTable);


        // -------------------------------------------------
        // EVENTS TABLE
        // -------------------------------------------------

        String createEventsTable =
                "CREATE TABLE " + TABLE_EVENTS + " (" +

                        EVENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        EVENT_NAME +
                        " TEXT NOT NULL, " +

                        EVENT_DESCRIPTION +
                        " TEXT NOT NULL, " +

                        EVENT_DATE +
                        " TEXT NOT NULL, " +

                        EVENT_TIME +
                        " TEXT NOT NULL, " +

                        EVENT_VENUE +
                        " TEXT NOT NULL, " +

                        EVENT_ORGANIZER +
                        " TEXT NOT NULL, " +

                        EVENT_MAX_REGISTRATIONS +
                        " INTEGER NOT NULL DEFAULT 50" +

                        ")";

        db.execSQL(createEventsTable);


        // -------------------------------------------------
        // REGISTRATIONS TABLE
        // -------------------------------------------------

        String createRegistrationsTable =
                "CREATE TABLE " + TABLE_REGISTRATIONS + " (" +

                        REGISTRATION_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        REG_EVENT_ID +
                        " INTEGER NOT NULL, " +

                        REG_STUDENT_EMAIL +
                        " TEXT NOT NULL, " +

                        REG_STUDENT_NAME +
                        " TEXT NOT NULL, " +

                        "UNIQUE(" +
                        REG_EVENT_ID +
                        ", " +
                        REG_STUDENT_EMAIL +
                        ")" +

                        ")";

        db.execSQL(createRegistrationsTable);
    }


    // =====================================================
    // DATABASE UPGRADE
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        // -------------------------------------------------
        // VERSION 1 -> VERSION 2
        // CREATE EVENTS TABLE
        // -------------------------------------------------

        if (oldVersion < 2) {

            String createEventsTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_EVENTS + " (" +

                            EVENT_ID +
                            " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            EVENT_NAME +
                            " TEXT NOT NULL, " +

                            EVENT_DESCRIPTION +
                            " TEXT NOT NULL, " +

                            EVENT_DATE +
                            " TEXT NOT NULL, " +

                            EVENT_TIME +
                            " TEXT NOT NULL, " +

                            EVENT_VENUE +
                            " TEXT NOT NULL, " +

                            EVENT_ORGANIZER +
                            " TEXT NOT NULL" +

                            ")";

            db.execSQL(createEventsTable);
        }


        // -------------------------------------------------
        // VERSION 2 -> VERSION 3
        // CREATE REGISTRATIONS TABLE
        // -------------------------------------------------

        if (oldVersion < 3) {

            String createRegistrationsTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_REGISTRATIONS + " (" +

                            REGISTRATION_ID +
                            " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            REG_EVENT_ID +
                            " INTEGER NOT NULL, " +

                            REG_STUDENT_EMAIL +
                            " TEXT NOT NULL, " +

                            REG_STUDENT_NAME +
                            " TEXT NOT NULL, " +

                            "UNIQUE(" +
                            REG_EVENT_ID +
                            ", " +
                            REG_STUDENT_EMAIL +
                            ")" +

                            ")";

            db.execSQL(createRegistrationsTable);
        }


        // -------------------------------------------------
        // VERSION 3 -> VERSION 4
        // ADD EVENT CAPACITY
        // -------------------------------------------------

        if (oldVersion < 4) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_EVENTS +
                            " ADD COLUMN " +
                            EVENT_MAX_REGISTRATIONS +
                            " INTEGER NOT NULL DEFAULT 50"
            );
        }
    }


    // =====================================================
    // USER FUNCTIONS
    // =====================================================

    public boolean registerUser(
            String name,
            String email,
            String password,
            String role) {

        name = name == null ? "" : name.trim();

        email = email == null
                ? ""
                : email.trim().toLowerCase(Locale.ROOT);

        password = password == null
                ? ""
                : password.trim();

        role = role == null
                ? ""
                : role.trim();


        // Validate fields
        if (name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                role.isEmpty()) {

            return false;
        }


        // Check duplicate email
        if (checkEmail(email)) {

            return false;
        }


        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                name
        );

        values.put(
                COLUMN_EMAIL,
                email
        );

        values.put(
                COLUMN_PASSWORD,
                password
        );

        values.put(
                COLUMN_ROLE,
                role
        );


        long result =
                db.insert(
                        TABLE_USERS,
                        null,
                        values
                );

        db.close();

        return result != -1;
    }


    // =====================================================
    // INSERT USER
    // =====================================================

    public boolean insertUser(
            String name,
            String email,
            String password,
            String role) {

        return registerUser(
                name,
                email,
                password,
                role
        );
    }


    // =====================================================
    // CHECK EMAIL
    // =====================================================

    public boolean checkEmail(String email) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        email = email == null
                ? ""
                : email.trim();

        Cursor cursor =
                db.rawQuery(
                        "SELECT " +
                                COLUMN_ID +
                                " FROM " +
                                TABLE_USERS +
                                " WHERE LOWER(" +
                                COLUMN_EMAIL +
                                ") = LOWER(?)",

                        new String[]{
                                email
                        }
                );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }


    // =====================================================
    // CHECK LOGIN
    // =====================================================

    public boolean checkUser(
            String email,
            String password,
            String role) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        email = email == null
                ? ""
                : email.trim();

        password = password == null
                ? ""
                : password.trim();

        role = role == null
                ? ""
                : role.trim();


        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                COLUMN_ID +
                                " FROM " +
                                TABLE_USERS +

                                " WHERE LOWER(" +
                                COLUMN_EMAIL +
                                ") = LOWER(?)" +

                                " AND " +
                                COLUMN_PASSWORD +
                                " = ?" +

                                " AND LOWER(" +
                                COLUMN_ROLE +
                                ") = LOWER(?)",

                        new String[]{
                                email,
                                password,
                                role
                        }
                );


        boolean validUser =
                cursor.moveToFirst();

        cursor.close();
        db.close();

        return validUser;
    }


    // =====================================================
    // GET USER NAME
    // =====================================================

    public String getUserName(String email) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        email = email == null
                ? ""
                : email.trim();

        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                COLUMN_NAME +

                                " FROM " +
                                TABLE_USERS +

                                " WHERE LOWER(" +
                                COLUMN_EMAIL +
                                ") = LOWER(?)",

                        new String[]{
                                email
                        }
                );


        String name = "User";

        if (cursor.moveToFirst()) {

            int index =
                    cursor.getColumnIndex(
                            COLUMN_NAME
                    );

            if (index >= 0) {

                String databaseName =
                        cursor.getString(index);

                if (databaseName != null &&
                        !databaseName.trim().isEmpty()) {

                    name = databaseName;
                }
            }
        }


        cursor.close();
        db.close();

        return name;
    }


    // =====================================================
    // EVENT FUNCTIONS
    // =====================================================


    // =====================================================
    // INSERT EVENT
    // OLD METHOD
    // DEFAULT CAPACITY = 50
    // =====================================================

    public boolean insertEvent(
            String eventName,
            String description,
            String date,
            String time,
            String venue,
            String organizer) {

        return insertEvent(
                eventName,
                description,
                date,
                time,
                venue,
                organizer,
                50
        );
    }


    // =====================================================
    // INSERT EVENT WITH CAPACITY
    // =====================================================

    public boolean insertEvent(
            String eventName,
            String description,
            String date,
            String time,
            String venue,
            String organizer,
            int maxRegistrations) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                EVENT_NAME,
                eventName
        );

        values.put(
                EVENT_DESCRIPTION,
                description
        );

        values.put(
                EVENT_DATE,
                date
        );

        values.put(
                EVENT_TIME,
                time
        );

        values.put(
                EVENT_VENUE,
                venue
        );

        values.put(
                EVENT_ORGANIZER,
                organizer
        );

        values.put(
                EVENT_MAX_REGISTRATIONS,
                maxRegistrations
        );


        long result =
                db.insert(
                        TABLE_EVENTS,
                        null,
                        values
                );

        db.close();

        return result != -1;
    }

// =====================================================
// UPDATE EVENT
// =====================================================

    public boolean updateEvent(
            int eventId,
            String eventName,
            String description,
            String date,
            String time,
            String venue,
            String organizer,
            int maxRegistrations) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(EVENT_NAME, eventName);
        values.put(EVENT_DESCRIPTION, description);
        values.put(EVENT_DATE, date);
        values.put(EVENT_TIME, time);
        values.put(EVENT_VENUE, venue);
        values.put(EVENT_ORGANIZER, organizer);
        values.put(EVENT_MAX_REGISTRATIONS, maxRegistrations);

        int rowsUpdated = db.update(
                TABLE_EVENTS,
                values,
                EVENT_ID + " = ?",
                new String[]{
                        String.valueOf(eventId)
                }
        );

        db.close();

        return rowsUpdated > 0;
    }
    // =====================================================
    // GET ALL EVENTS
    // FIXED VERSION
    // =====================================================

    public Cursor getAllEvents() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        /*
         * IMPORTANT:
         *
         * We use rawQuery here instead of db.query().
         *
         * This prevents Android from incorrectly treating
         * "event_id DESC" as the LIMIT value.
         */

        String query =
                "SELECT * " +
                        "FROM " +
                        TABLE_EVENTS +
                        " ORDER BY " +
                        EVENT_ID +
                        " DESC";

        return db.rawQuery(
                query,
                null
        );
    }


    // =====================================================
    // GET REGISTERED EVENTS
    // =====================================================

    public Cursor getRegisteredEvents(
            String studentEmail) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        String query =
                "SELECT e.* " +
                        "FROM events e " +
                        "INNER JOIN registrations r " +
                        "ON e.event_id = r.event_id " +
                        "WHERE LOWER(r.student_email) = LOWER(?) " +
                        "ORDER BY e.event_id DESC";

        return db.rawQuery(
                query,
                new String[]{
                        studentEmail == null
                                ? ""
                                : studentEmail.trim()
                }
        );
    }


    // =====================================================
    // GET TOTAL EVENT COUNT
    // =====================================================

    public int getEventCount() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_EVENTS,
                        null
                );

        int count = 0;

        if (cursor.moveToFirst()) {

            count =
                    cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }


    // =====================================================
    // GET UPCOMING EVENT COUNT
    // =====================================================

    public int getUpcomingEventCount() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                EVENT_DATE +
                                ", " +
                                EVENT_TIME +

                                " FROM " +
                                TABLE_EVENTS,

                        null
                );


        int count = 0;


        SimpleDateFormat format =
                new SimpleDateFormat(
                        "d/M/yyyy HH:mm",
                        Locale.getDefault()
                );

        format.setLenient(false);


        Date currentDateTime =
                new Date();


        while (cursor.moveToNext()) {

            String eventDate =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    EVENT_DATE
                            )
                    );

            String eventTime =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    EVENT_TIME
                            )
                    );


            try {

                String eventDateTime =
                        eventDate +
                                " " +
                                eventTime;


                Date eventDateObject =
                        format.parse(
                                eventDateTime
                        );


                if (eventDateObject != null &&
                        currentDateTime.before(
                                eventDateObject
                        )) {

                    count++;
                }

            } catch (ParseException e) {

                // Ignore invalid date/time
            }
        }


        cursor.close();
        db.close();

        return count;
    }


    // =====================================================
    // DELETE EVENT
    // =====================================================

    public boolean deleteEvent(int eventId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.beginTransaction();

        try {

            // Delete registrations belonging
            // to this event first
            db.delete(
                    TABLE_REGISTRATIONS,
                    REG_EVENT_ID + "=?",
                    new String[]{
                            String.valueOf(eventId)
                    }
            );


            int result =
                    db.delete(
                            TABLE_EVENTS,
                            EVENT_ID + "=?",
                            new String[]{
                                    String.valueOf(eventId)
                            }
                    );


            db.setTransactionSuccessful();

            return result > 0;

        } finally {

            db.endTransaction();
            db.close();
        }
    }


    // =====================================================
    // REGISTRATION FUNCTIONS
    // =====================================================


    // =====================================================
    // REGISTER STUDENT FOR EVENT
    // =====================================================

    public boolean registerForEvent(
            int eventId,
            String studentName,
            String studentEmail) {

        studentName =
                studentName == null
                        ? ""
                        : studentName.trim();

        studentEmail =
                studentEmail == null
                        ? ""
                        : studentEmail.trim()
                        .toLowerCase(Locale.ROOT);


        // -------------------------------------------------
        // Check duplicate registration
        // -------------------------------------------------

        if (isStudentRegistered(
                eventId,
                studentEmail)) {

            return false;
        }


        // -------------------------------------------------
        // Check event capacity
        // -------------------------------------------------

        if (isEventFull(eventId)) {

            return false;
        }


        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                REG_EVENT_ID,
                eventId
        );

        values.put(
                REG_STUDENT_NAME,
                studentName
        );

        values.put(
                REG_STUDENT_EMAIL,
                studentEmail
        );


        long result =
                db.insert(
                        TABLE_REGISTRATIONS,
                        null,
                        values
                );

        db.close();

        return result != -1;
    }

// =====================================================
// DEREGISTER STUDENT FROM EVENT
// =====================================================

    public boolean deregisterFromEvent(
            int eventId,
            String studentEmail) {

        studentEmail =
                studentEmail == null
                        ? ""
                        : studentEmail.trim()
                        .toLowerCase(Locale.ROOT);

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_REGISTRATIONS,

                        REG_EVENT_ID +
                                "=? AND " +
                                REG_STUDENT_EMAIL +
                                "=?",

                        new String[]{
                                String.valueOf(eventId),
                                studentEmail
                        }
                );

        db.close();

        return result > 0;
    }
    // =====================================================
    // CHECK IF STUDENT IS REGISTERED
    // =====================================================

    public boolean isStudentRegistered(
            int eventId,
            String studentEmail) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        studentEmail =
                studentEmail == null
                        ? ""
                        : studentEmail.trim();


        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                REGISTRATION_ID +

                                " FROM " +
                                TABLE_REGISTRATIONS +

                                " WHERE " +
                                REG_EVENT_ID +
                                " = ?" +

                                " AND LOWER(" +
                                REG_STUDENT_EMAIL +
                                ") = LOWER(?)",

                        new String[]{
                                String.valueOf(eventId),
                                studentEmail
                        }
                );


        boolean registered =
                cursor.moveToFirst();

        cursor.close();
        db.close();

        return registered;
    }


    // =====================================================
    // GET STUDENT'S REGISTERED EVENTS
    // =====================================================

    public Cursor getStudentRegisteredEvents(
            String studentEmail) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        String query =
                "SELECT events.* " +
                        "FROM events " +
                        "INNER JOIN registrations " +
                        "ON events.event_id = " +
                        "registrations.event_id " +
                        "WHERE LOWER(registrations.student_email) = LOWER(?) " +
                        "ORDER BY events.event_id DESC";


        return db.rawQuery(
                query,
                new String[]{
                        studentEmail == null
                                ? ""
                                : studentEmail.trim()
                }
        );
    }


    // =====================================================
    // GET TOTAL REGISTRATIONS
    // =====================================================

    public int getRegistrationCount() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT COUNT(*) " +
                                "FROM " +
                                TABLE_REGISTRATIONS,

                        null
                );


        int count = 0;

        if (cursor.moveToFirst()) {

            count =
                    cursor.getInt(0);
        }


        cursor.close();
        db.close();

        return count;
    }


    // =====================================================
    // GET REGISTRATION COUNT FOR ONE EVENT
    // =====================================================

    public int getEventRegistrationCount(
            int eventId) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT COUNT(*) " +
                                "FROM " +
                                TABLE_REGISTRATIONS +

                                " WHERE " +
                                REG_EVENT_ID +
                                " = ?",

                        new String[]{
                                String.valueOf(eventId)
                        }
                );


        int count = 0;

        if (cursor.moveToFirst()) {

            count =
                    cursor.getInt(0);
        }


        cursor.close();
        db.close();

        return count;
    }


    // =====================================================
    // GET MAXIMUM REGISTRATIONS FOR EVENT
    // =====================================================

    public int getEventMaxRegistrations(
            int eventId) {

        SQLiteDatabase db =
                this.getReadableDatabase();


        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                EVENT_MAX_REGISTRATIONS +

                                " FROM " +
                                TABLE_EVENTS +

                                " WHERE " +
                                EVENT_ID +
                                " = ?",

                        new String[]{
                                String.valueOf(eventId)
                        }
                );


        int maxRegistrations = 50;


        if (cursor.moveToFirst()) {

            int index =
                    cursor.getColumnIndex(
                            EVENT_MAX_REGISTRATIONS
                    );

            if (index >= 0) {

                maxRegistrations =
                        cursor.getInt(index);
            }
        }


        cursor.close();
        db.close();

        return maxRegistrations;
    }


    // =====================================================
    // CHECK IF EVENT IS FULL
    // =====================================================

    public boolean isEventFull(
            int eventId) {

        int registeredCount =
                getEventRegistrationCount(
                        eventId
                );


        int maxRegistrations =
                getEventMaxRegistrations(
                        eventId
                );


        return registeredCount >=
                maxRegistrations;
    }


    // =====================================================
    // GET ALL REGISTRATIONS FOR ADMIN
    // =====================================================

    public Cursor getAllRegistrations() {

        SQLiteDatabase db =
                this.getReadableDatabase();


        String query =
                "SELECT " +

                        "r.registration_id, " +
                        "r.event_id, " +
                        "r.student_name, " +
                        "r.student_email, " +

                        "e.event_name, " +
                        "e.event_date, " +
                        "e.event_time, " +
                        "e.venue, " +
                        "e.organizer, " +
                        "e.max_registrations " +

                        "FROM registrations r " +

                        "INNER JOIN events e " +

                        "ON r.event_id = e.event_id " +

                        "ORDER BY r.registration_id DESC";


        return db.rawQuery(
                query,
                null
        );
    }
}