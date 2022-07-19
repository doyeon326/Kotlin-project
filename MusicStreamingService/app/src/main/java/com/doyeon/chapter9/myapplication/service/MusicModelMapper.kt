package com.doyeon.chapter9.myapplication.service

fun MusicEntity.mapper(id: Long): MusicModel =
    MusicModel(
        id = id,
        streamUrl = this.streamUrl,
        coverUrl = this.coverUrl,
        track = this.track,
        artist = this.artist
    )

fun MusicDto.mapper(): PlayerModel =
    PlayerModel(
        playMusicList = musics.mapIndexed() { index, musicEntity ->

            musicEntity.mapper(index.toLong())
        }
    )