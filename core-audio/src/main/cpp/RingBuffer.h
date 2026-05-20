#ifndef AETHER_RING_BUFFER_H
#define AETHER_RING_BUFFER_H

#include <atomic>
#include <cstdint>

struct RingBufferEntry {
    int32_t command;
    float value;
};

class RingBuffer {
public:
    explicit RingBuffer(int capacity);

    ~RingBuffer();

    bool push(const RingBufferEntry& entry);
    bool pop(RingBufferEntry& entry);
    int size() const;
    int capacity() const { return capacity_; }
    bool empty() const { return size() == 0; }
    bool full() const {
        int h = head_.load(std::memory_order_acquire);
        int t = tail_.load(std::memory_order_acquire);
        return advance(h) == t;
    }

private:
    int advance(int index) const { return (index + 1) % capacity_; }

    RingBufferEntry* buffer_;
    int capacity_;
    std::atomic<int> head_;
    std::atomic<int> tail_;
};

#endif
