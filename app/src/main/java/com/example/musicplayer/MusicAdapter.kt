package com.example.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

private var mediaPlayer: MediaPlayer ?= null
private var playingPosition = -1                        // To track which item is playing
private var previousPlayPauseBtn: ImageView? = null     // To track previous play/pause button

class MusicAdapter(val context: Context, val musicDataList: List<Data>): RecyclerView.Adapter<MusicAdapter.myViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        // Creates the view in case Layout Manager fails to create view for the data
        val itemView = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false)
        return myViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return musicDataList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        // Populates data into the view
        val musicData = musicDataList[position]
        Log.d("MusicAdapter", "onBindViewHolder: " + musicData)
        holder.trackTitle.text = musicData.title
        holder.albumTitle.text = musicData.album.title

        // Using Picasso Library to load the image using Image URL
        Picasso.get()
            .load(musicData.album.cover)    // URL of image to be load
            .placeholder(R.drawable.music_album)    // Default image to show while loading
            .into(holder.albumImage)

        holder.bind(context, musicData.preview.toUri(), position)
    }

    class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var trackTitle: TextView
        var albumTitle: TextView
        var albumImage: ImageView
        val playPauseBtn: ImageView
        val stopBtn: ImageView

        init {
            trackTitle = itemView.findViewById(R.id.trackTitle)
            albumTitle = itemView.findViewById(R.id.albumTitle)
            albumImage = itemView.findViewById(R.id.albumImage)
            playPauseBtn = itemView.findViewById(R.id.playPauseBtn)
            stopBtn = itemView.findViewById(R.id.stopBtn)
        }

        fun bind(context: Context, uri: Uri, position: Int) {

            playPauseBtn.setOnClickListener {
                if(playingPosition == position) {
                    if(mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.pause()
                        playPauseBtn.setImageResource(R.drawable.ic_play)
                    }
                    else {
                        mediaPlayer?.start()
                        playPauseBtn.setImageResource(R.drawable.ic_pause)
                    }
                }
                else {
                    // If another item is selected, stopping and releasing previous media player
                    if(mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.stop()
                        mediaPlayer?.reset()
                        mediaPlayer = null
                        playingPosition = -1
                        previousPlayPauseBtn?.setImageResource(R.drawable.ic_play)  // Updating previous item play pause button to ic_play
                    }
                }

                try {
                    if(mediaPlayer == null) {
                        // Initialize and start new media player
                        mediaPlayer = MediaPlayer.create(context, uri)
                        mediaPlayer?.start()
                        playPauseBtn.setImageResource(R.drawable.ic_pause)
                        previousPlayPauseBtn = playPauseBtn
                        playingPosition = position
                        mediaPlayer?.setOnCompletionListener {
                            playingPosition = -1
                            playPauseBtn.setImageResource(R.drawable.ic_play)
                            mediaPlayer?.release()
                            mediaPlayer = null
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            stopBtn.setOnClickListener {
                if(playingPosition == position) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                    playingPosition = -1
                    playPauseBtn.setImageResource(R.drawable.ic_play)
                }
            }

            // Resets the buttons in case view is being recycled
            if(playingPosition != position) {
                playPauseBtn.setImageResource(R.drawable.ic_play)
            }
            else if(mediaPlayer?.isPlaying == true) {
                playPauseBtn.setImageResource(R.drawable.ic_pause)
            }

        }
    }

}