package com.doyeon.chapter12.mygithubrepository.utility

import com.doyeon.chapter12.mygithubrepository.data.entity.GithubRepoEntity
import com.doyeon.chapter12.mygithubrepository.data.response.GithubRepoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/repositories")
    suspend fun searchRepositories(@Query("q") query: String): Response<GithubRepoSearchResponse>

    @GET("repos/{owner}/{name}")
    suspend fun getRepository(
        @Path("owner") ownerLogin: String,
        @Path("name") repoName: String
    ): Response<GithubRepoEntity>

}