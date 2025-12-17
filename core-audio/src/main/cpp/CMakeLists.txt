cmake_minimum_required(VERSION 3.22.1)

project("aether-audio")

add_library(
    aether-audio
    SHARED
    audio_engine.cpp
)

target_include_directories(
    aether-audio
    PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
)

target_compile_features(
    aether-audio
    PUBLIC
    cxx_std_17
)
