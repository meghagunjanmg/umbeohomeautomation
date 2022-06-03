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
public final class RealysDao_Impl implements RealysDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RelayModel> __insertionAdapterOfRelayModel;

  private final SharedSQLiteStatement __preparedStmtOfNukeTable;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOne;

  private final SharedSQLiteStatement __preparedStmtOfUpdateState;

  public RealysDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRelayModel = new EntityInsertionAdapter<RelayModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `devicerelays` (`pid`,`relay_name`,`relayState`,`deviceip`,`ds`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RelayModel value) {
        stmt.bindLong(1, value.getPid());
        if (value.getRelay_name() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRelay_name());
        }
        if (value.getRelayState() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getRelayState());
        }
        if (value.getDeviceip() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDeviceip());
        }
        if (value.getDs() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDs());
        }
      }
    };
    this.__preparedStmtOfNukeTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM devicerelays";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateOne = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update devicerelays SET relay_name =? WHERE pid =?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateState = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update devicerelays SET relayState =? WHERE pid =?";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final List<RelayModel> entities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRelayModel.insert(entities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
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
  public void UpdateState(final int id, final String dev) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateState.acquire();
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
      __preparedStmtOfUpdateState.release(_stmt);
    }
  }

  @Override
  public LiveData<List<RelayModel>> getAll() {
    final String _sql = "SELECT * FROM devicerelays ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"devicerelays"}, false, new Callable<List<RelayModel>>() {
      @Override
      public List<RelayModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPid = CursorUtil.getColumnIndexOrThrow(_cursor, "pid");
          final int _cursorIndexOfRelayName = CursorUtil.getColumnIndexOrThrow(_cursor, "relay_name");
          final int _cursorIndexOfRelayState = CursorUtil.getColumnIndexOrThrow(_cursor, "relayState");
          final int _cursorIndexOfDeviceip = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceip");
          final int _cursorIndexOfDs = CursorUtil.getColumnIndexOrThrow(_cursor, "ds");
          final List<RelayModel> _result = new ArrayList<RelayModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RelayModel _item;
            final int _tmpPid;
            _tmpPid = _cursor.getInt(_cursorIndexOfPid);
            final String _tmpRelay_name;
            _tmpRelay_name = _cursor.getString(_cursorIndexOfRelayName);
            final String _tmpRelayState;
            _tmpRelayState = _cursor.getString(_cursorIndexOfRelayState);
            final String _tmpDeviceip;
            _tmpDeviceip = _cursor.getString(_cursorIndexOfDeviceip);
            final String _tmpDs;
            _tmpDs = _cursor.getString(_cursorIndexOfDs);
            _item = new RelayModel(_tmpPid,_tmpRelay_name,_tmpRelayState,_tmpDeviceip,_tmpDs);
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
  public List<RelayModel> loadAll() {
    final String _sql = "SELECT * FROM devicerelays";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPid = CursorUtil.getColumnIndexOrThrow(_cursor, "pid");
      final int _cursorIndexOfRelayName = CursorUtil.getColumnIndexOrThrow(_cursor, "relay_name");
      final int _cursorIndexOfRelayState = CursorUtil.getColumnIndexOrThrow(_cursor, "relayState");
      final int _cursorIndexOfDeviceip = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceip");
      final int _cursorIndexOfDs = CursorUtil.getColumnIndexOrThrow(_cursor, "ds");
      final List<RelayModel> _result = new ArrayList<RelayModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final RelayModel _item;
        final int _tmpPid;
        _tmpPid = _cursor.getInt(_cursorIndexOfPid);
        final String _tmpRelay_name;
        _tmpRelay_name = _cursor.getString(_cursorIndexOfRelayName);
        final String _tmpRelayState;
        _tmpRelayState = _cursor.getString(_cursorIndexOfRelayState);
        final String _tmpDeviceip;
        _tmpDeviceip = _cursor.getString(_cursorIndexOfDeviceip);
        final String _tmpDs;
        _tmpDs = _cursor.getString(_cursorIndexOfDs);
        _item = new RelayModel(_tmpPid,_tmpRelay_name,_tmpRelayState,_tmpDeviceip,_tmpDs);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
