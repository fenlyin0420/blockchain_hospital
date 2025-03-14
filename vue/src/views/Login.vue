<template>
  <!-- 背景为视频 -->
  <div class="container">
    <video autoplay loop muted class="background-video" preload="auto">
      <source src="@/assets/videos/bg7.mp4?" type="video/mp4">
      您的浏览器不支持HTML5视频。
    </video>
    <!-- 粒子效果 -->
    <vue-particles color="#7eb488" :particleOpacity="0.7" :particlesNumber="120" shapeType="circle" :particleSize="4"
      linesColor="#7eb488" :linesWidth="1" :lineLinked="true" :lineOpacity="0.4" :linesDistance="150" :moveSpeed="3"
      :hoverEffect="true" hoverMode="grab" :clickEffect="true" clickMode="push" class="particles"></vue-particles>

    <div class="login-form">
      <div
        style="  text-align: center; font-size: 20px; font-weight: bold; margin-bottom: 20px; color: #333; text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);">
        欢 迎 登 录</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username" label="账号" label-width="15%">
          <el-input :style="{ width: '100%' }" prefix-icon="el-icon-user" placeholder="请输入账号"
            v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码" label-width="15%">
          <el-input :style="{ width: '100%' }" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password
            v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="身份" label-width="15%">
          <el-select :style="{ width: '100%' }" v-model="form.role" placeholder="请选择角色">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="医生" value="DOCTOR"></el-option>
            <el-option label="护士" value="NURSE"></el-option>
            <el-option label="患者" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label-width="15%" style="text-align: center;">
          <el-button style="width: 100%; background-color: #7eb488; border-color: #7eb488; color: white"
            @click="login">登 录</el-button>
        </el-form-item>
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            还没有账号？请 <a href="/register">注册</a>
          </div>
        </div>
      </el-form>
    </div>
  </div>


  <!-- 原版 -->
  <!-- <div class="container">
      <div style="width: 400px; padding: 30px; background-color: aliceblue; border-radius: 5px;">
        <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">欢迎登录</div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="username" label="账号" label-width="15%">
            <el-input :style="{ width: '100%' }" prefix-icon="el-icon-user" placeholder="请输入账号"
              v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码" label-width="15%">
            <el-input :style="{ width: '100%' }" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password
              v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item label="身份" label-width="15%">
            <el-select :style="{ width: '100%' }" v-model="form.role" placeholder="请选择角色">
              <el-option label="管理员" value="ADMIN"></el-option>
              <el-option label="医生" value="DOCTOR"></el-option>
              <el-option label="护士" value="NURSE"></el-option>
              <el-option label="患者" value="USER"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label-width="15%" style="text-align: center;">
            <el-button style="width: 100%; background-color: #7eb488; border-color: #7eb488; color: white"
              @click="login">登 录</el-button>
          </el-form-item>
          <div style="display: flex; align-items: center">
            <div style="flex: 1"></div>
            <div style="flex: 1; text-align: right">
              还没有账号？请 <a href="/register">注册</a>
            </div>
          </div>
        </el-form>
      </div>
  </div> -->
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: {},
      dialogVisible: true,
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              //key: xm-user     value:转成字符串的res.data
              localStorage.setItem("xm-user", JSON.stringify(res.data))
              if (res.data.role === 'USER') {
                this.$router.push('/doctorCard')
              }
              else {
                this.$router.push('/')  // 跳转主页
              }
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  /* background-image: url("@/assets/imgs/bg.jpg"); */
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

a {
  color: #2a60c9;
}

::v-deep .el-input__inner {
  font-family: "SimSun", "宋体", serif;
  color: blue;
  font-size: 16px;
}

/* 视频动画 */
.background-video {
  position: absolute;
  overflow: hidden;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -2;
}

.particles {
  position: absolute;
  overflow: hidden;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.login-form {
  position: relative;
  z-index: 2;
  /* 半透明背景 */
  background: rgba(255, 255, 255, 1);
  border-radius: 8px;
  width: 400px;
  padding: 30px;
  /* 白色背景 */
  /* background-color: white;*/
  box-shadow: 0 0 5px rgba(63, 146, 78, 1);
  transform: translateZ(0); 
  backface-visibility: hidden;
}

</style>