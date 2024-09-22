<template>
  <div class="container">
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">欢迎登录医院预约挂号系统</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%;">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="医生" value="DOCTOR"></el-option>
            <el-option label="护士" value="NURSE"></el-option>
            <el-option label="患者" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%; background-color: #7eb488; border-color: #7eb488; color: white" @click="login">登 录</el-button>
        </el-form-item>
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            还没有账号？请 <a href="/register">注册</a>
          </div>
        </div>
      </el-form>
    </div>
<!--    <el-dialog title="郑重声明" :visible.sync="dialogVisible" :show-close="false" width="40%" :close-on-click-modal="false" destroy-on-close>-->
<!--      <div style="color: red; font-size: 16px; line-height: 26px; margin-bottom: 20px">-->
<!--        本项目由知识星球【项目训练营】原创，我们已经对本项目<strong style="color: #4d1cc0">申请了软件著作权，完善了所有星球项目相关的法律文件材料</strong>，星球内付费用户可以学习该项目自己使用，禁止在其他平台做转手或者倒卖，-->
<!--        我们有专门人员在各大平台不定期检索，一旦发现，<strong style="color: #4d1cc0">我们第一时间收集好证据，向你发送律师函</strong>，现在互联网所有账号都是实名制，-->
<!--        我们可以明确追溯到你。请大家不要做违法的事情，不要给你的人生轨迹抹上一层黑。-->
<!--      </div>-->
<!--      <div style="text-align: center">-->
<!--        <div>微信扫码加入知识星球【项目训练营】</div>-->
<!--        <img src="https://i.postimg.cc/0yTm5ps0/image.jpg" alt="">-->
<!--      </div>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="dialogVisible=false">我已明确本项目仅限个人使用</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </div>
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
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$router.push('/')  // 跳转主页
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
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
</style>