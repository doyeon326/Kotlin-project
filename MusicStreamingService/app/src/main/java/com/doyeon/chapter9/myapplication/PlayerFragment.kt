package com.doyeon.chapter9.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.doyeon.chapter9.myapplication.databinding.FragmentPlayerBinding
import com.doyeon.chapter9.myapplication.service.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayerFragment: Fragment(R.layout.fragment_player) {

    private var model = PlayerModel()
    private var binding: FragmentPlayerBinding? = null

    private lateinit  var playListAdapter: PlayListAdapter
    private var simplePlayer: SimpleExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getVideoListFromServer()
        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding
        initPlayListButton(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)
        initPlayerView(fragmentPlayerBinding)
        initPlayControlButtons(fragmentPlayerBinding)
    }

    private fun initPlayControlButtons(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playControlImageView.setOnClickListener {
            val player = this.simplePlayer ?: return@setOnClickListener

            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        fragmentPlayerBinding.skipNextImageView.setOnClickListener {
            val nextMusic = model.nextMusic() ?: return@setOnClickListener
            playMusic(nextMusic)
        }

        fragmentPlayerBinding.skipPreviousImageView.setOnClickListener {
            val prevMusic = model.prevNextMusic() ?: return@setOnClickListener
            playMusic(prevMusic)
        }
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        playListAdapter = PlayListAdapter {
            //todo 음악을 재생
            playMusic(it)

        }
        fragmentPlayerBinding.playerListRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)

        }
    }

    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
         fragmentPlayerBinding.playlistImageView.setOnClickListener {
             //todo 만약 서버에서 데이터가 다 불러오지 않은 생태일 때
             if (model.currentPosition == -1 ){
                  return@setOnClickListener
             }
             fragmentPlayerBinding.playerViewGroup.isVisible = model.isWatchingPlayListView
             fragmentPlayerBinding.playListViewGroup.isVisible = model.isWatchingPlayListView.not()

             model.isWatchingPlayListView = !model.isWatchingPlayListView
         }
    }

    private fun getVideoListFromServer() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MusicService::class.java)
            .also {
                it.listMusics()
                    .enqueue(object: Callback<MusicDto> {
                        override fun onResponse(
                            call: Call<MusicDto>,
                            response: Response<MusicDto>
                        ) {


                            Log.d("PlayerFragment","${response.body() }")

                            response.body()?.let { musicDto ->

                                model = musicDto.mapper()

                                setMusicList(model.getAdapterModels() )
                                playListAdapter.submitList(model.getAdapterModels())
                            }
                        }

                        override fun onFailure(call: Call<MusicDto>, t: Throwable) {

                        }

                    })
            }

    }

    private fun setMusicList(modelList: List<MusicModel>) {
       context?.let {
           simplePlayer?.addMediaItems(modelList.map { musicModel ->  
               MediaItem.Builder()
                   .setMediaId(musicModel.id.toString())
                   .setUri(musicModel.streamUrl)
                   .build()
           })

           simplePlayer?.prepare()
           //simplePlayer?.play()
       }
    }

    private fun playMusic(musicModel: MusicModel) {
        model.updateCurrentPosition(musicModel)
        simplePlayer?.seekTo(model.currentPosition, 0)
        simplePlayer?.play()
    }

    private fun initPlayerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        context?.let {
            simplePlayer = SimpleExoPlayer.Builder(it).build()
        }

        fragmentPlayerBinding.playerView.player = simplePlayer
        binding?.let { binding ->
            simplePlayer?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if (isPlaying) {
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_pause_48)
                    } else {
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }


                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                     val newIndex = mediaItem?.mediaId ?: return
                    model.currentPosition = newIndex.toInt()
                    playListAdapter.submitList(model.getAdapterModels() )

                }
            })

        }

    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}