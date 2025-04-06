<!-- FloatingCharacter.vue -->
<template>
  <div
    class="floating-character"
    :class="{ 'is-dragging': isDragging }"
    :style="{
      left: isRight ? 'auto' : posLeft + 'px',
      right: isRight ? posRight + 'px' : 'auto',
      top: posTop + 'px'
    }"
    @mousedown.prevent="startDrag"
    @click="handleClick"
    @touchstart.prevent="startDrag"
    @touchmove.prevent="onDrag"
    @touchend.prevent="stopDrag"
  >
    <div class="character">
      <!-- 可以使用img标签而不是背景图 -->
      <img src="@/assets/imgs/AI.png" alt="角色" class="character-image" />
      <div class="click-effect"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FloatingCharacter',
  props: {
    targetRoute: {
      type: String,
      default: '/'
    },
    dragThreshold: {
      type: Number,
      default: 5
    },
    size: {
      type: Number,
      default: 60
    },
    snapDelay: {
      type: Number,
      default: 800 // 悬停后多久吸附到边缘，单位毫秒
    }
  },
  data() {
    return {
      posLeft: 0, // 距离左边的距离
      posRight: 20, // 距离右边的距离
      posTop: 100, // 距离顶部的距离
      isRight: true, // 是否靠右显示
      isDragging: false,
      startX: 0,
      startY: 0,
      hasDragged: false,
      initialX: 0,
      initialY: 0,
      snapTimer: null
    }
  },
  mounted() {
    this.loadPosition()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('mousemove', this.onDrag)
    window.removeEventListener('mouseup', this.stopDrag)
    window.removeEventListener('resize', this.handleResize)
    if (this.snapTimer) {
      clearTimeout(this.snapTimer)
    }
  },
  computed: {
    elementSize() {
      return this.size
    }
  },
  methods: {
    // 初始化位置
    loadPosition() {
      const savedPos = localStorage.getItem('floatingPos')
      if (savedPos) {
        try {
          const { left, right, top, isRight } = JSON.parse(savedPos)
          this.posLeft = left
          this.posRight = right
          this.posTop = top
          this.isRight = isRight
        } catch (error) {
          console.error('解析保存的位置失败:', error)
          this.resetPosition()
        }
      } else {
        this.resetPosition()
      }
    },

    // 重置到右下角
    resetPosition() {
      this.isRight = true
      this.posRight = 20
      this.posLeft = 0
      this.posTop = window.innerHeight / 2 - this.size / 2
      this.savePosition()
    },

    // 保存位置
    savePosition() {
      localStorage.setItem('floatingPos', JSON.stringify({
        left: this.posLeft,
        right: this.posRight,
        top: this.posTop,
        isRight: this.isRight
      }))
    },

    // 开始拖动
    startDrag(e) {
      // 如果定时器存在，取消吸附
      if (this.snapTimer) {
        clearTimeout(this.snapTimer)
        this.snapTimer = null
      }

      this.isDragging = true
      this.hasDragged = false
      
      const clientX = e.touches ? e.touches[0].clientX : e.clientX
      const clientY = e.touches ? e.touches[0].clientY : e.clientY
      
      this.startX = clientX
      this.startY = clientY
      
      // 获取元素位置信息
      const rect = this.$el.getBoundingClientRect()
      this.initialX = rect.left
      this.initialY = rect.top
      
      window.addEventListener('mousemove', this.onDrag)
      window.addEventListener('mouseup', this.stopDrag)
    },

    // 拖动处理
    onDrag(e) {
      if (!this.isDragging) return

      const clientX = e.touches ? e.touches[0].clientX : e.clientX
      const clientY = e.touches ? e.touches[0].clientY : e.clientY

      // 计算移动距离
      const deltaX = clientX - this.startX
      const deltaY = clientY - this.startY

      // 判断是否达到拖动阈值
      if (Math.abs(deltaX) > this.dragThreshold || Math.abs(deltaY) > this.dragThreshold) {
        this.hasDragged = true
      }

      // 计算新位置 - 现在是绝对位置，不是相对位置
      let newX = this.initialX + deltaX
      let newY = this.initialY + deltaY

      // 边界限制
      newX = Math.max(0, Math.min(newX, window.innerWidth - this.size))
      newY = Math.max(0, Math.min(newY, window.innerHeight - this.size))

      // 根据当前模式更新位置
      this.posTop = newY
      this.posLeft = newX
      this.posRight = window.innerWidth - newX - this.size
      
      // 在拖动时不改变isRight，等待释放后决定
    },

    // 停止拖动
    stopDrag() {
      if (this.isDragging) {
        this.isDragging = false
        
        // 设置定时器，在释放后等待一段时间再吸附
        this.snapTimer = setTimeout(() => {
          this.snapToEdge()
        }, this.snapDelay)
      }
      
      window.removeEventListener('mousemove', this.onDrag)
      window.removeEventListener('mouseup', this.stopDrag)
    },

    // 吸附到边缘
    snapToEdge() {
      // 计算到左右两边的距离
      const distanceToLeft = this.posLeft
      const distanceToRight = this.posRight
      
      // 决定吸附到哪一边
      if (distanceToLeft < distanceToRight) {
        // 吸附到左边
        this.isRight = false
        this.posLeft = 0
      } else {
        // 吸附到右边
        this.isRight = true
        this.posRight = 0
      }
      
      // 保存位置
      this.savePosition()
    },

    // 点击处理
    handleClick() {
      if (!this.hasDragged) {
        if (this.targetRoute) {
          this.$router.push(this.targetRoute);
        } else {
          this.$emit('click');
        }
      }
      this.hasDragged = false;
    },

    // 窗口大小变化处理
    handleResize() {
      // 确保位置在可视区域内
      this.posTop = Math.min(this.posTop, window.innerHeight - this.size)
      
      if (this.isRight) {
        // 如果靠右，右侧位置不变
        this.posLeft = window.innerWidth - this.size - this.posRight
      } else {
        // 如果靠左，左侧位置不变
        this.posRight = window.innerWidth - this.size - this.posLeft
      }
      
      this.savePosition()
    }
  }
}
</script>

<style scoped>
.floating-character {
  position: fixed;
  z-index: 9999;
  cursor: grab;
  touch-action: none;
  transition: all 0.3s cubic-bezier(0.18, 0.89, 0.32, 1.28);
}

.floating-character.is-dragging {
  transition: none !important;
}

.character {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
  position: relative;
  overflow: visible;
  animation: float 3s ease-in-out infinite;
}

.character-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  /* 允许图片显示原始形状，包括透明区域 */
  filter: drop-shadow(0 4px 12px rgba(0, 0, 0, 0.15));
}

.click-effect {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80%;
  height: 80%;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.2s;
  pointer-events: none; /* 确保点击事件穿透到图片 */
}

.floating-character:active .click-effect {
  opacity: 1;
}

.floating-character.is-dragging .character {
  animation: none !important;
}

/* 添加悬停效果 */
.character:hover {
  transform: scale(1.1);
}

.character-image:hover {
  filter: drop-shadow(0 6px 16px rgba(0, 0, 0, 0.2));
}

/* 添加动画效果 */
@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-5px);
  }
  100% {
    transform: translateY(0px);
  }
}
</style>