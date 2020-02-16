package at.fh.swengb.mattlschweiger.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_or_edit_note.*
import java.util.*

class NewOrEditNote : AppCompatActivity()
{


    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when(item?.itemId)
        {
            R.id.save -> {
                val token = intent.getStringExtra("ACCESS_TOKEN")
                val title = new_or_edit_editText_title.text.toString()
                val text = new_or_edit_editText_text.text.toString()
                if (title.isNotBlank() || text.isNotBlank()) {
                    val noteID = intent.getStringExtra("NOTE_ID")
                    var note: Note
                    if (noteID == "no_id")
                    {
                        note = Note(id = UUID.randomUUID().toString(),title = title, text = text, toUpload = true)
                        NoteRepository.addNote(this, note)
                        NoteRepository.addOrUpdate(note, token,
                            success = {
                                note.toUpload = false
                                NoteRepository.updateNote(this, note)
                            },
                            error = {
                                val msg: String
                                when (it)
                                {
                                    90 -> msg = getString(R.string.error_msg_internet)
                                    else -> msg = getString(R.string.error_msg_other)
                                }
                                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                            })
                    }
                    else
                    {
                        note = Note(id = noteID,title = title, text = text, toUpload = true)
                        NoteRepository.updateNote(this, note)
                        NoteRepository.addOrUpdate(note, token,
                            success = {
                                note.toUpload = false
                                NoteRepository.updateNote(this, note)
                            },
                            error = {
                                val msg: String
                                when (it) {
                                    90 -> msg = getString(R.string.error_msg_internet)
                                    else -> msg = getString(R.string.error_msg_other)
                                }
                                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                            })
                    }
                    finish()
                } else
                {
                    Toast.makeText(this, getString(R.string.error_title_and_text_empty), Toast.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_or_edit_note)

        val noteID = intent.getStringExtra("NOTE_ID")

        if (noteID != "no_id")
        {
            val note = NoteRepository.noteById(this, noteID)

            new_or_edit_editText_title.setText(note.title)
            new_or_edit_editText_text.setText(note.text)
        }
    }
}
