package com.algostack.nir.services.model

data class PublicPostResponse(
    val `data`: List<PublicPostData>,
    val status: Int
)