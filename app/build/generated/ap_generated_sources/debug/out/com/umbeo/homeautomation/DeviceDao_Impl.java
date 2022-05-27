package com.umbeo.homeautomation;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DeviceDao_Impl implements DeviceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DeviceModel> __insertionAdapterOfDeviceModel;

  private final EntityInsertionAdapter<DeviceModel> __insertionAdapterOfDeviceModel_1;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOne2;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOne;

  private final SharedSQLiteStatement __preparedStmtOfNukeTable;

  public DeviceDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDeviceModel = new EntityInsertionAdapter<DeviceModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `devices` (`pid`,`device_name`,`new_name`,`device_status`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DeviceModel value) {
        stmt.bindLong(1, value.getPid());
        if (value.getDevice_name() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDevice_name());
        }
        if (value.getNew_name() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNew_name());
        }
        stmt.bindLong(4, value.getDevice_status());
      }
    };
    this.__insertionAdapterOfDeviceModel_1 = new EntityInsertionAdapter<DeviceModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `devices` (`pid`,`device_name`,`new_name`,`device_status`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DeviceModel value) {
        stmt.bindLong(1, value.getPid());
        if (value.getDevice_name() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDevice_name());
        }
        if (value.getNew_name() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNew_name());
        }
        stmt.bindLong(4, value.getDevice_status());
      }
    };
    this.__preparedStmtOfUpdateOne2 = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update devices SET device_status =? WHERE pid =?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateOne = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update devices SET new_name =? WHERE pid =?";
        return _query;
      }
    };
    this.__preparedStmtOfNukeTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM devices";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final List<DeviceModel> entities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDeviceModel.insert(entities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final DeviceModel devices) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDeviceModel_1.insert(devices);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void UpdateOne2(final int id, final int dev) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOne2.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, dev);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateOne2.release(_stmt);
    }
  }

  @Override
  public void UpdateOne(final int id, final String dev) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOne.acquire();
    int _argIndex = 1;
    if (dev == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, dev);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateOne.release(_stmt);
    }
  }

  @Override
  public void nukeTable() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfNukeTable.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfNukeTable.release(_stmt);
    }
  }

  @Override
  public LiveData<List<DeviceModel>> getAll() {
    final String _sql = "SELECT * FROM devices";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"devices"}, false, new Callable<List<DeviceModel>>() {
      @Override
      public List<DeviceModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPid = CursorUtil.getColumnIndexOrThrow(_cursor, "pid");
          final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
          final int _cursorIndexOfNewName = CursorUtil.getColumnIndexOrThrow(_cursor, "new_name");
          final int _cursorIndexOfDeviceStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "device_status");
          final List<DeviceModel> _result = new ArrayList<DeviceModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DeviceModel _item;
            final int _tmpPid;
            _tmpPid = _cursor.getInt(_cursorIndexOfPid);
            final String _tmpDevice_name;
            _tmpDevice_name = _cursor.getString(_cursorIndexOfDeviceName);
            final String _tmpNew_name;
            _tmpNew_name = _cursor.getString(_cursorIndexOfNewName);
            final int _tmpDevice_status;
            _tmpDevice_status = _cursor.getInt(_cursorIndexOfDeviceStatus);
            _item = new DeviceModel(_tmpPid,_tmpDevice_name,_tmpNew_name,_tmpDevice_status);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<DeviceModel> loadAll() {
    final String _sql = "SELECT * FROM devices";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPid = CursorUtil.getColumnIndexOrThrow(_cursor, "pid");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfNewName = CursorUtil.getColumnIndexOrThrow(_cursor, "new_name");
      final int _cursorIndexOfDeviceStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "device_status");
      final List<DeviceModel> _result = new ArrayList<DeviceModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DeviceModel _item;
        final int _tmpPid;
        _tmpPid = _cursor.getInt(_cursorIndexOfPid);
        final String _tmpDevice_name;
        _tmpDevice_name = _cursor.getString(_cursorIndexOfDeviceName);
        final String _tmpNew_name;
        _tmpNew_name = _cursor.getString(_cursorIndexOfNewName);
        final int _tmpDevice_status;
        _tmpDevice_status = _cursor.getInt(_cursorIndexOfDeviceStatus);
        _item = new DeviceModel(_tmpPid,_tmpDevice_name,_tmpNew_name,_tmpDevice_status);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<DeviceModel> getItemById(final String dev) {
    final String _sql = "SELECT * from devices WHERE device_name= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dev == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, dev);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPid = CursorUtil.getColumnIndexOrThrow(_cursor, "pid");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfNewName = CursorUtil.getColumnIndexOrThrow(_cursor, "new_name");
      final int _cursorIndexOfDeviceStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "device_status");
      final List<DeviceModel> _result = new ArrayList<DeviceModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DeviceModel _item;
        final int _tmpPid;
        _tmpPid = _cursor.getInt(_cursorIndexOfPid);
        final String _tmpDevice_name;
        _tmpDevice_name = _cursor.getString(_cursorIndexOfDeviceName);
        final String _tmpNew_name;
        _tmpNew_name = _cursor.getString(_cursorIndexOfNewName);
        final int _tmpDevice_status;
        _tmpDevice_status = _cursor.getInt(_cursorIndexOfDeviceStatus);
        _item = new DeviceModel(_tmpPid,_tmpDevice_name,_tmpNew_name,_tmpDevice_status);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
