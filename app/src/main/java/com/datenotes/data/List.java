package com.datenotes.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;

@Entity
public class List implements Parcelable {
    public static final String KEY = "key_list";
    public static final Long DEFAULT_ID = null;
    public static final String DEFAULT_NAME = "New list";

    @Id
    private Long id;
    private String name;

    @ToMany(referencedJoinProperty = "listId")
    @OrderBy("date DESC")
    private java.util.List<Note> notes;

    public List() {
        this(DEFAULT_NAME);
    }

    public List(String name) {
        this.name = name;
        notes = new ArrayList<>();
        id = DEFAULT_ID;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        List baseList = (List) o;

        if (name != null ? !name.equals(baseList.name) : baseList.name != null) return false;
        return notes != null ? notes.equals(baseList.notes) : baseList.notes == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(id);
        parcel.writeString(name);
        parcel.writeTypedList(notes);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(java.util.List<Note> notes) {
        this.notes = notes;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Keep
    @Generated(hash = 526753486)
    public java.util.List<Note> getNotes() {
        if (notes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NoteDao targetDao = daoSession.getNoteDao();
            java.util.List<Note> notesNew = targetDao._queryList_Notes(id);
            synchronized (this) {
                if (notes == null) {
                    notes = notesNew;
                }
            }
        }
        return notes;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 2032098259)
    public synchronized void resetNotes() {
        notes = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public static final Creator<List> CREATOR = new Creator<List>() {
        @Override
        public List[] newArray(int size) {
            return new List[size];
        }

        @Override
        public List createFromParcel(Parcel source) {
            return new List(source);
        }
    };
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 92363602)
    private transient ListDao myDao;

    public List(Parcel source) {
        id = (Long) source.readValue(Long.class.getClassLoader());
        name = source.readString();
        notes = source.createTypedArrayList(Note.CREATOR);
    }

    @Generated(hash = 798240094)
    public List(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addNote(Note note) {
        daoSession.insert(note);
        notes.add(0, note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
        daoSession.delete(note);
    }

    public void updateNote(Note note) {
        for (Note n : notes) {
            if (n.getId() == note.getId()) {
                n.setNote(note.getNote());
                n.setDate(note.getDate());
                daoSession.update(note);
                break;
            }
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 736766358)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getListDao() : null;
    }
}
