package at.fh.swengb.mattlschweiger.notes

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



object NoteRepository
{

    fun login(
        username: String,
        password: String,
        success: (authResponse: AuthResponse) -> Unit,
        error: (errorCode: Int) -> Unit
    ) {
        NotesApi.retrofitService.login(AuthRequest(username, password))
            .enqueue(object: Callback<AuthResponse> {
                override fun onFailure(call: Call<AuthResponse>, t: Throwable)
                {
                    error(90)
                }

                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>)
                {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null)
                    {
                        success(responseBody)
                    }
                    else
                    {
                        error(91)
                    }
                }
            })
    }

    fun addOrUpdate(
        note: Note,
        token: String,
        success: (returnedNote: Note) -> Unit,
        error: (errorCode: Int) -> Unit
    )
    {
        NotesApi.retrofitService.addOrUpdateNote(token, note).enqueue(object: Callback<Note>
        {
            override fun onFailure(call: Call<Note>, t: Throwable) {
                error(90)
            }

            override fun onResponse(call: Call<Note>, response: Response<Note>)
            {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    error(91)
                }
            }
        })
    }

    fun notesListFromApi(token: String,
                         lastSync: Long,
                         success: (note: NotesResponse) -> Unit,
                         error: (errorCode: Int) -> Unit) {
        NotesApi.retrofitService.notes(token, lastSync).enqueue(object: Callback<NotesResponse>
        {
            override fun onFailure(call: Call<NotesResponse>, t: Throwable)
            {
                error(90)
            }

            override fun onResponse(call: Call<NotesResponse>, response: Response<NotesResponse>)
            {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    error (91)
                }
            }
        })
    }

    fun addNote(context: Context, note: Note)
    {
        val applicationContext = context.applicationContext
        val db = NoteDatabase.getDatabase(applicationContext)
        db.noteDao.insert(note)
    }

    fun updateNote(context: Context, note: Note)
    {
        val applicationContext = context.applicationContext
        val db = NoteDatabase.getDatabase(applicationContext)
        db.noteDao.update(note)
    }

    fun notesList(context: Context): List<Note>
    {
        val applicationContext = context.applicationContext
        val db = NoteDatabase.getDatabase(applicationContext)
        return db.noteDao.allNotes()
    }

    fun noteById(context: Context, id: String): Note
    {
        val applicationContext = context.applicationContext
        val db = NoteDatabase.getDatabase(applicationContext)
        return db.noteDao.noteByID(id)
    }

    fun deleteAll(context: Context)
    {
        val applicationContext = context.applicationContext
        val db = NoteDatabase.getDatabase(applicationContext)
        db.noteDao.deleteAllNotes()
    }
}