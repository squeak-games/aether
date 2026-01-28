#include "RingBuffer.h"

RingBuffer::RingBuffer(int capacity)
    : capacity_(capacity)
    , head_(0)
    , tail_(0) {
    buffer_ = new RingBufferEntry[capacity_];
}

RingBuffer::~RingBuffer() {
    delete[] buffer_;
}

bool RingBuffer::push(const RingBufferEntry& entry) {
    int currentHead = head_.load(std::memory_order_relaxed);
    int currentTail = tail_.load(std::memory_order_acquire);
    if (advance(currentHead) == currentTail) {
        return false;
    }
    buffer_[currentHead] = entry;
    head_.store(advance(currentHead), std::memory_order_release);
    return true;
}

bool RingBuffer::pop(RingBufferEntry& entry) {
    int currentHead = head_.load(std::memory_order_acquire);
    int currentTail = tail_.load(std::memory_order_relaxed);
    if (currentHead == currentTail) {
        return false;
    }
    entry = buffer_[currentTail];
    tail_.store(advance(currentTail), std::memory_order_release);
    return true;
}

int RingBuffer::size() const {
    int h = head_.load(std::memory_order_acquire);
    int t = tail_.load(std::memory_order_acquire);
    if (h >= t) return h - t;
    return capacity_ - t + h;
}
