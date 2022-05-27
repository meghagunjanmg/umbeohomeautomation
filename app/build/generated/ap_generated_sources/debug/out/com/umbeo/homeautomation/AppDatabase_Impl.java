package com.umbeo.homeautomation;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile DeviceDao _deviceDao;

  private volatile RealysDao _realysDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `devices` (`pid` INTEGER NOT NULL, `device_name` TEXT, `new_name` TEXT, `device_status` INTEGER NOT NULL, PRIMARY KEY(`pid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `devicerelays` (`pid` INTEGER NOT NULL, `relay_name` TEXT, `relayState` TEXT, PRIMARY KEY(`pid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '07f3390b0057a5cd0b23dd935c4dddf1')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `devices`");
        _db.execSQL("DROP TABLE IF EXISTS `devicerelays`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsDevices = new HashMap<String, TableInfo.Column>(4);
        _columnsDevices.put("pid", new TableInfo.Column("pid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("device_name", new TableInfo.Column("device_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("new_name", new TableInfo.Column("new_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevices.put("device_status", new TableInfo.Column("device_status", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDevices = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDevices = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDevices = new TableInfo("devices", _columnsDevices, _foreignKeysDevices, _indicesDevices);
        final TableInfo _existingDevices = TableInfo.read(_db, "devices");
        if (! _infoDevices.equals(_existingDevices)) {
          return new RoomOpenHelper.ValidationResult(false, "devices(com.umbeo.homeautomation.DeviceModel).\n"
                  + " Expected:\n" + _infoDevices + "\n"
                  + " Found:\n" + _existingDevices);
        }
        final HashMap<String, TableInfo.Column> _columnsDevicerelays = new HashMap<String, TableInfo.Column>(3);
        _columnsDevicerelays.put("pid", new TableInfo.Column("pid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevicerelays.put("relay_name", new TableInfo.Column("relay_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevicerelays.put("relayState", new TableInfo.Column("relayState", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDevicerelays = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDevicerelays = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDevicerelays = new TableInfo("devicerelays", _columnsDevicerelays, _foreignKeysDevicerelays, _indicesDevicerelays);
        final TableInfo _existingDevicerelays = TableInfo.read(_db, "devicerelays");
        if (! _infoDevicerelays.equals(_existingDevicerelays)) {
          return new RoomOpenHelper.ValidationResult(false, "devicerelays(com.umbeo.homeautomation.RelayModel).\n"
                  + " Expected:\n" + _infoDevicerelays + "\n"
                  + " Found:\n" + _existingDevicerelays);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "07f3390b0057a5cd0b23dd935c4dddf1", "bef6d99226f4bd31db6d271bbbd66f5b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "devices","devicerelays");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `devices`");
      _db.execSQL("DELETE FROM `devicerelays`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public DeviceDao deviceDao() {
    if (_deviceDao != null) {
      return _deviceDao;
    } else {
      synchronized(this) {
        if(_deviceDao == null) {
          _deviceDao = new DeviceDao_Impl(this);
        }
        return _deviceDao;
      }
    }
  }

  @Override
  public RealysDao realysDao() {
    if (_realysDao != null) {
      return _realysDao;
    } else {
      synchronized(this) {
        if(_realysDao == null) {
          _realysDao = new RealysDao_Impl(this);
        }
        return _realysDao;
      }
    }
  }
}
