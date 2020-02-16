package at.fh.swengb.mattlschweiger.notes

import androidx.room.*

@Dao
interface NotesDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note:Note)

    @Query("SELECT * FROM Note ORDER BY Title")
    fun allNotes(): List<Note>

    @Query("SELECT * FROM Note WHERE id = :id")
    fun noteByID(id: String): Note

    @Query("DELETE FROM Note")
    fun deleteAllNotes()
}