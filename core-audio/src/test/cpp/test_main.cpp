#include <cassert>
#include <cmath>
#include <cstdio>
#include "RingBuffer.h"
#include "AdsrEnvelope.h"
#include "BiquadFilter.h"
#include "SpatialPanner.h"
#include "VoiceManager.h"

static int testsPassed = 0;
static int testsFailed = 0;

#define TEST(name) void test_##name()
#define RUN_TEST(name) do { \
    test_##name(); \
    testsPassed++; \
    printf("  PASS: %s\n", #name); \
} while(0)
#define ASSERT_TRUE(cond) do { \
    if (!(cond)) { \
        printf("  FAIL: %s (line %d): %s\n", __func__, __LINE__, #cond); \
        testsFailed++; \
        return; \
    } \
} while(0)
#define ASSERT_EQ(a, b) ASSERT_TRUE((a) == (b))
#define ASSERT_NEAR(a, b, eps) ASSERT_TRUE(std::abs((a) - (b)) < (eps))

TEST(ringBuffer_pushPop) {
    RingBuffer buf(4);

    ASSERT_TRUE(buf.empty());
    ASSERT_EQ(0, buf.size());

    RingBufferEntry e1{1, 440.0f};
    ASSERT_TRUE(buf.push(e1));
    ASSERT_EQ(1, buf.size());

    RingBufferEntry out{};
    ASSERT_TRUE(buf.pop(out));
    ASSERT_EQ(1, out.command);
    ASSERT_NEAR(440.0f, out.value, 0.001f);
    ASSERT_TRUE(buf.empty());
}

TEST(ringBuffer_full) {
    RingBuffer buf(4);

    ASSERT_TRUE(buf.push({1, 1.0f}));
    ASSERT_TRUE(buf.push({2, 2.0f}));
    ASSERT_TRUE(buf.push({3, 3.0f}));

    ASSERT_TRUE(buf.full());
    ASSERT_TRUE(!buf.push({4, 4.0f}));
}

TEST(ringBuffer_wrapAround) {
    RingBuffer buf(3);

    buf.push({1, 1.0f});
    buf.push({2, 2.0f});
    RingBufferEntry out;
    buf.pop(out);
    buf.pop(out);
    buf.push({3, 3.0f});
    buf.push({4, 4.0f});

    ASSERT_EQ(2, buf.size());
    ASSERT_TRUE(!buf.empty());
}

TEST(adsr_envelope_noteOnOff) {
    AdsrEnvelope env;
    env.setSampleRate(48000);
    env.setAttack(1.0f);
    env.setDecay(1.0f);
    env.setSustain(0.5f);
    env.setRelease(1.0f);

    ASSERT_EQ(AdsrEnvelope::State::Idle, env.state());

    env.noteOn();
    ASSERT_EQ(AdsrEnvelope::State::Attack, env.state());

    float level = env.process();
    ASSERT_TRUE(level > 0.0f);
}

TEST(adsr_envelope_reachesSustain) {
    AdsrEnvelope env;
    env.setSampleRate(100);
    env.setAttack(10.0f);
    env.setDecay(10.0f);
    env.setSustain(0.5f);
    env.setRelease(100.0f);

    env.noteOn();

    for (int i = 0; i < 5000; i++) {
        env.process();
    }

    ASSERT_EQ(AdsrEnvelope::State::Sustain, env.state());
}

TEST(biquad_lowPass_passthrough) {
    BiquadFilter filter(BiquadFilter::Type::LowPass);
    filter.setSampleRate(48000);
    filter.setCutoff(20000.0f);
    filter.setQ(0.707f);

    float output = filter.process(1.0f);
    ASSERT_TRUE(output > 0.0f);

    filter.reset();
    for (int i = 0; i < 100; i++) {
        filter.process(0.5f);
    }
}

TEST(spatialPanner_centerAtOrigin) {
    SpatialPanner panner;
    panner.setListenerPosition(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    panner.setSourcePosition(0.0f, 0.0f, 1.0f);

    PanGains gains = panner.process();
    ASSERT_NEAR(gains.left, gains.right, 0.01f);
}

TEST(spatialPanner_pansToLeft) {
    SpatialPanner panner;
    panner.setListenerPosition(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    panner.setSourcePosition(-1.0f, 0.0f, 0.0f);

    PanGains gains = panner.process();
    ASSERT_TRUE(gains.left > gains.right);
}

TEST(voiceManager_addAndRemove) {
    VoiceManager vm;

    VoiceParams p1;
    p1.frequency = 440.0f;
    p1.amplitude = 0.5f;

    int id1 = vm.addVoice(p1);
    ASSERT_TRUE(id1 >= 0);

    int id2 = vm.addVoice(p1);
    ASSERT_TRUE(id2 >= 0);
    ASSERT_TRUE(id2 != id1);

    ASSERT_EQ(2, vm.activeVoiceCount());

    float sample = vm.process(48000);
    ASSERT_TRUE(sample >= -1.0f && sample <= 1.0f);
}

TEST(voiceManager_filterAffectsOutput) {
    VoiceManager vm;

    VoiceParams p;
    p.frequency = 440.0f;
    p.amplitude = 1.0f;

    vm.addVoice(p);
    float unfiltered = vm.process(48000);

    vm.setMasterFilter(BiquadFilter::Type::LowPass, 200.0f, 0.707f);
    float filtered = vm.process(48000);

    ASSERT_TRUE(unfiltered != filtered);
}

int main() {
    printf("Aether C++ Unit Tests\n");
    printf("====================\n");

    RUN_TEST(ringBuffer_pushPop);
    RUN_TEST(ringBuffer_full);
    RUN_TEST(ringBuffer_wrapAround);
    RUN_TEST(adsr_envelope_noteOnOff);
    RUN_TEST(adsr_envelope_reachesSustain);
    RUN_TEST(biquad_lowPass_passthrough);
    RUN_TEST(spatialPanner_centerAtOrigin);
    RUN_TEST(spatialPanner_pansToLeft);
    RUN_TEST(voiceManager_addAndRemove);
    RUN_TEST(voiceManager_filterAffectsOutput);

    printf("\n");
    printf("Results: %d passed, %d failed\n", testsPassed, testsFailed);

    return testsFailed > 0 ? 1 : 0;
}
