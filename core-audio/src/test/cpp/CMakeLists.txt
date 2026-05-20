cmake_minimum_required(VERSION 3.22.1)

project("aether-audio-host-tests")

# Disable Oboe for host testing — provide stubs
add_definitions(-DAETHER_HOST_TEST=1)

add_executable(
    aether-audio-tests
    ../../main/cpp/RingBuffer.cpp
    ../../main/cpp/AdsrEnvelope.cpp
    ../../main/cpp/BiquadFilter.cpp
    ../../main/cpp/SpatialPanner.cpp
    ../../main/cpp/VoiceManager.cpp
    test_main.cpp
)

target_include_directories(
    aether-audio-tests
    PRIVATE
    ../../main/cpp
)

target_compile_features(
    aether-audio-tests
    PUBLIC
    cxx_std_17
)

target_link_libraries(
    aether-audio-tests
    m
)
