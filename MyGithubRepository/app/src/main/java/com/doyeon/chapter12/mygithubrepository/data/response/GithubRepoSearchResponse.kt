package com.doyeon.chapter12.mygithubrepository.data.response

import com.doyeon.chapter12.mygithubrepository.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)