package com.bombadu.pixashot


import com.google.gson.annotations.SerializedName

data class ImageResponse(
    var hits: List<Hit>,
    var total: Int,
    var totalHits: Int
) {
    data class Hit(
        var comments: Int,
        var downloads: Int,
        var favorites: Int,
        var id: Int,
        var imageHeight: Int,
        var imageSize: Int,
        var imageWidth: Int,
        var largeImageURL: String,
        var likes: Int,
        var pageURL: String,
        var previewHeight: Int,
        var previewURL: String,
        var previewWidth: Int,
        var tags: String,
        var type: String,
        var user: String,
        @SerializedName("user_id")
        var userId: Int,
        var userImageURL: String,
        var views: Int,
        var webformatHeight: Int,
        var webformatURL: String,
        var webformatWidth: Int
    )
}