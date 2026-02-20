package com.cmp.community.healers.softskilltraining

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform