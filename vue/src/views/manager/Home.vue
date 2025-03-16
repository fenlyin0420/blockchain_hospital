<template>
  <div>
    <div class="card" style="padding: 15px; background : #264387;">
      <span style="color: white;">您好，{{ user.name }}！欢迎使用本系统</span>
    </div>

    <div style="width: 100%;" class="card">
    <el-carousel :interval="5000" arrow="always">
        <el-carousel-item v-for="(item, index) in images" :key="index">
          <img :src="item" alt="Carousel Image" style="width: 100%; height: 100%; object-fit: cover;">
        </el-carousel-item>
    </el-carousel>
    </div>

    <div style="display: flex; flex-wrap: wrap; gap: 20px; margin: 10px 0;">
      <!-- 公告列表 -->
      <div class="card" style="flex: 1; max-width: 50%; padding: 20px; border-radius: 8px;">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold;">公告列表</div>
        <div>
          <el-timeline reverse slot="reference">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
              <el-popover placement="right" width="200" trigger="hover" :content="item.content">
                <span slot="reference">{{ item.title }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>

      <!-- 院内新闻 -->
      <div class="card" style="flex: 1; max-width: 50%; padding: 20px; border-radius: 8px;">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold;">院内新闻</div>
        <div>
          <el-timeline reverse slot="reference">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
              <el-popover placement="right" width="200" trigger="hover" :content="item.content">
                <span slot="reference">{{ item.content }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [],
      images: [
      require("@/assets/imgs/slideshow1.jpg"), 
        require("@/assets/imgs/slideshow2.jpg"),
        require("@/assets/imgs/slideshow3.jpg"),
        require("@/assets/imgs/slideshow4.jpg"),
        require("@/assets/imgs/slideshow5.jpg"),
        require("@/assets/imgs/slideshow6.jpg")
      ]
    }
  },
  created() {
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
