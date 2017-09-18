package com.youtube.database.classes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table NOTE.
*/
public class YouTubeModelDao extends AbstractDao<YouTubeModel, Long> {

    public static final String TABLENAME = "YOUTUBE";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Title = new Property(1, String.class, "title", false, "title");
        public final static Property Desc = new Property(2, String.class, "desc", false, "desc");
        public final static Property Image = new Property(3, String.class, "image", false, "image");
        public final static Property Tab = new Property(4, String.class, "tab", false, "tab");
       // public final static Property Date = new Property(3, java.util.Date.class, "date", false, "DATE");
    };


    public YouTubeModelDao(DaoConfig config) {
        super(config);
    }

    public YouTubeModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
      /*  db.execSQL("CREATE TABLE " + constraint + "'NOTE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TEXT' TEXT NOT NULL ," + // 1: text
                "'COMMENT' TEXT," + // 2: comment
                "'DATE' INTEGER);"); // 3: date*/

       /* public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Title = new Property(1, String.class, "title", false, "title");
        public final static Property Desc = new Property(2, String.class, "desc", false, "desc");
        public final static Property Image = new Property(3, String.class, "image", false, "image");
        public final static Property Tab = new Property(4, String.class, "tab", false, "tab");*/


       /* db.execSQL("CREATE TABLE " + constraint + "'"+TABLENAME+"' (" + //
                "'id' INTEGER PRIMARY KEY ," + // 0: id
                "'title' TEXT NOT NULL ," + // 1: text
                "'desc' TEXT," + // 2: comment
                "'image' TEXT," + // 3: comment
                "'tab' TEXT);"); // 4: date
*/
        db.execSQL("CREATE TABLE " + constraint + "'"+TABLENAME+"' (" + //
                "'id' INTEGER PRIMARY KEY ," + // 0: id
                "'title' TEXT NOT NULL ," + // 1: text
                "'desc' TEXT," + // 2: comment
                "'image' TEXT UNIQUE," + // 3: comment
                "'tab' TEXT);"); // 4: date


        //"'DATE' INTEGER);"); // 3: date


    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'"+TABLENAME+"'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, YouTubeModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getStrTitle());
 
        String comment = entity.getStrDesc();
        if (comment != null) {
            stmt.bindString(3, comment);
        }


        String image = entity.getStrImage();
        if (image != null) {
            stmt.bindString(4, image);
        }


        String tab = entity.getStrDesc();
        if (tab != null) {
            stmt.bindString(5, tab);
        }
/*
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date.getTime());
        }*/
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public YouTubeModel readEntity(Cursor cursor, int offset) {
        YouTubeModel entity = new YouTubeModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // desc
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // desc
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4)

           //cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)) // date
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, YouTubeModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStrTitle(cursor.getString(offset + 1));
        entity.setStrDesc(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStrImage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStrTab(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));

        //entity.setDate(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(YouTubeModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(YouTubeModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
