package at.fh.swengb.mattlschweiger.notes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_notes_list.*
import java.util.*



class NotesList : AppCompatActivity()
{

    val noteAdapter = NoteAdapter()
    {
        val intent = Intent(this, NewOrEditNote::class.java)
        val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val token = sharedPref.getString("ACCESS_TOKEN","")
        intent.putExtra("NOTE_ID", it.id)
        intent.putExtra("ACCESS_TOKEN", token)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when(item?.itemId)
        {
            R.id.new_note -> {
                val intent = Intent(this, NewOrEditNote::class.java)
                val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                val token = sharedPref.getString("ACCESS_TOKEN","")

                with(intent) {
                    putExtra("NOTE_ID", "no_id")
                    putExtra("ACCESS_TOKEN", token)
                }

                startActivity(intent)

                true
            }
            R.id.logout ->
            {
                NoteRepository.deleteAll(this)

                val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

                sharedPreferences.edit().clear().apply()
                finish()

                true
            }
            R.id.sync ->
            {
                val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                val lastSync = sharedPref.getLong("LAST_SYNC", 0)
                val token = sharedPref.getString("ACCESS_TOKEN", "") ?: ""

                sharedPref.edit().putLong("LAST_SYNC", java.util.Calendar.getInstance().timeInMillis)

                NoteRepository.notesListFromApi(token, lastSync,
                    success = {
                        val offlineNotes = NoteRepository.notesList(this).map { x -> x.id }
                        val onlineNotes = it.notes

                        for (i in onlineNotes)
                        {
                            if (i.id in offlineNotes)
                            {
                                NoteRepository.updateNote(this, i)
                            } else {
                                NoteRepository.addNote(this, i)
                            }
                        }

                        noteAdapter.updateList(NoteRepository.notesList(this))
                    },
                    error =
                    {
                        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                    })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)


        val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val lastSync = sharedPref.getLong("LAST_SYNC", 0)
        val token = sharedPref.getString("ACCESS_TOKEN", "") ?: ""

        noteAdapter.updateList(NoteRepository.notesList(this))
        notes_recycler_view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        notes_recycler_view.adapter = noteAdapter

        NoteRepository.notesListFromApi(token, lastSync,
            success = {
                sharedPref.edit().putLong("LAST_SYNC", java.util.Calendar.getInstance().timeInMillis).apply()

                val offlineNotes = NoteRepository.notesList(this).map { x -> x.id }
                val onlineNotes = it.notes

                for (i in onlineNotes)
                {
                    if (i.id in offlineNotes)
                    {
                        NoteRepository.updateNote(this, i)
                    } else {
                        NoteRepository.addNote(this, i)
                    }
                }

                noteAdapter.updateList(NoteRepository.notesList(this))
            },
            error = {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            })
    }

    override fun onResume()
    {
        super.onResume()

        val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val lastSync = sharedPref.getLong("LAST_SYNC", 0)
        val token = sharedPref.getString("ACCESS_TOKEN", "") ?: ""

        noteAdapter.updateList(NoteRepository.notesList(this))

        NoteRepository.notesListFromApi(token, lastSync,
            success = {
                sharedPref.edit().putLong("LAST_SYNC", java.util.Calendar.getInstance().timeInMillis).apply()

                val offlineNotes = NoteRepository.notesList(this).map { x -> x.id }
                val onlineNotes = it.notes

                for (i in onlineNotes) {
                    if (i.id in offlineNotes) {
                        NoteRepository.updateNote(this, i)
                    } else {
                        NoteRepository.addNote(this, i)
                    }
                }

                noteAdapter.updateList(NoteRepository.notesList(this))
            },
            error = {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            })
    }

    companion object
    {
        val NOTE_ID = "NOTE_ID"
        val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}
