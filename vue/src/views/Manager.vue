<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left" @click="$router.push('/home')">
        <img src="@/assets/imgs/logo.png" />
        <div class="title" style="color: white;">基于SM算法和多模态AI的全场景区块链医疗系统</div>
      </div>

      <!-- 面包屑导航栏 -->
      <div class="manager-header-center">
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="
                user.avatar ||
                'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
              " />
            <div style="color: white; font-size: 18px;">{{ user.name || "管理员" }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="PersonalInformation">密钥管理</el-dropdown-item>
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/home')">回到首页</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu :unique-opened="true" router style="border: none" :default-active="$route.path">
          <!-- 患者模块 -->
          <el-submenu index="userModule" v-if="user.role === 'USER'">
            <template slot="title">
              <img src="../assets/imgs/patient.png" alt="" class="el-icon-img">
              <span>患者模块</span>
            </template>
            <el-menu-item index="/doctorCard">预约挂号</el-menu-item>
            <el-menu-item index="/reserve">预约管理</el-menu-item>
            <el-menu-item index="/caseList">溯源病历</el-menu-item>
          </el-submenu>
          <el-submenu v-else index="userModule" disabled>
            <template slot="title">
              <img src="../assets/imgs/patient.png" alt="" class="el-icon-img">
              <span>患者模块</span>
            </template>
            <el-menu-item index="/doctorCard" disabled>预约挂号</el-menu-item>
            <el-menu-item index="/reserve" disabled>预约管理</el-menu-item>
            <el-menu-item index="/caseList" disabled>溯源病历</el-menu-item>
          </el-submenu>

          <!-- 医生模块 -->
          <el-submenu index="doctorModule" v-if="user.role === 'DOCTOR'">
            <template slot="title">
              <img src="../assets/imgs/doctor.png" alt="" class="el-icon-img">
              <span>医生模块</span>
            </template>
            <el-menu-item index="/doctorPlan" v-if="user.role === 'DOCTOR'">门诊排班</el-menu-item>
            <el-menu-item index="/doctorReserve" v-if="user.role === 'DOCTOR'">门诊业务</el-menu-item>
            <el-menu-item index="/hospitalization" v-if="user.role === 'DOCTOR'">住院业务</el-menu-item>
            <el-menu-item index="/doctorCaseList" v-if="user.role === 'DOCTOR'">病历列表</el-menu-item>
          </el-submenu>
          <el-submenu v-else index="doctorModule" disabled>
            <template slot="title">
              <img src="../assets/imgs/doctor.png" alt="" class="el-icon-img">
              <span>医生模块</span>
            </template>
            <el-menu-item index="/doctorPlan" v-if="user.role === 'DOCTOR'">门诊排班</el-menu-item>
            <el-menu-item index="/doctorReserve" v-if="user.role === 'DOCTOR'">门诊业务</el-menu-item>
            <el-menu-item index="/hospitalization" v-if="user.role === 'DOCTOR'">住院业务</el-menu-item>
            <el-menu-item index="/doctorCaseList" v-if="user.role === 'DOCTOR'">病历列表</el-menu-item>
          </el-submenu>


          <!-- 护士模块 -->
          <el-submenu index="nurseModule" v-if="user.role === 'NURSE'">
            <template slot="title">
              <img src="../assets/imgs/nurse.png" alt="" class="el-icon-img">
              <span>护士模块</span>
            </template>
            <el-menu-item index="/HealthCheck">健康检测</el-menu-item>
            <el-menu-item index="/AssignBeds">分配床位</el-menu-item>
            <el-menu-item index="/DailyCare">日常护理</el-menu-item>
            <el-menu-item index="/NursePlan">护士排班</el-menu-item>
            <el-menu-item index="/NurseRecord">病床分配记录</el-menu-item>
          </el-submenu>
          <el-submenu v-else index="nurseModule" disabled="">
            <template slot="title">
              <img src="../assets/imgs/nurse.png" alt="" class="el-icon-img">
              <span>护士模块</span>
            </template>
            <el-menu-item index="/HealthCheck">健康检测</el-menu-item>
            <el-menu-item index="/AssignBeds">分配床位</el-menu-item>
            <el-menu-item index="/DailyCare">日常护理</el-menu-item>
            <el-menu-item index="/NursePlan">护士排班</el-menu-item>
            <el-menu-item index="/NurseRecord">病床分配记录</el-menu-item>
          </el-submenu>

          <!-- 转诊模块 -->
          <el-submenu index="Transfer" v-if="user.role === 'ADMIN' || user.role === 'DOCTOR'">
            <template slot="title">
              <img src="../assets/imgs/referral.png" alt="" class="el-icon-img">
              <span>转诊模块</span>
            </template>

            <el-menu-item index="/referralApplication">转诊发起</el-menu-item>
            <el-menu-item index="/referralOut">转出审批</el-menu-item>
            <el-menu-item index="/referralIn">转诊接收</el-menu-item>
            <el-menu-item index="/receivePatients">转诊执行</el-menu-item>
            <el-menu-item index="/referralRecord">转诊记录</el-menu-item>
          </el-submenu>

          <!-- 管理模块 -->
          <el-submenu index="adminModule" v-if="user.role === 'ADMIN'">
            <template slot="title">
              <img src="../assets/imgs/admin.png" alt="" class="el-icon-img">
              <span>管理模块</span>
            </template>
            <el-menu-item index="/log">系统监控</el-menu-item>
            <el-menu-item index="/notice">公告信息</el-menu-item>
            <el-menu-item index="/hospital">全景视图</el-menu-item>
            <el-menu-item index="/department">科室信息</el-menu-item>
            <el-menu-item index="/drug">药品信息</el-menu-item>
            <el-menu-item index="/doctor">医生管理</el-menu-item>
            <el-menu-item index="/adminDoctorPlan">医生排班</el-menu-item>
            <el-menu-item index="/nurse">护士管理</el-menu-item>
            <el-menu-item index="/nurseplan">护士排班</el-menu-item>
            <el-menu-item index="/user">患者管理</el-menu-item>
            <el-menu-item index="/ward">病房管理</el-menu-item>
            <el-menu-item index="/approval">住院审批</el-menu-item>
          </el-submenu>
          <el-submenu v-else index="adminModule" disabled>
            <template slot="title">
              <img src="../assets/imgs/admin.png" alt="" class="el-icon-img">
              <span>管理模块</span>
            </template>
            <el-menu-item index="/log">系统监控</el-menu-item>
            <el-menu-item index="/notice">公告信息</el-menu-item>
            <el-menu-item index="/hospital">医院信息</el-menu-item>
            <el-menu-item index="/department">科室信息</el-menu-item>
            <el-menu-item index="/drug">药品信息</el-menu-item>
            <el-menu-item index="/doctor">医生管理</el-menu-item>
            <el-menu-item index="/adminDoctorPlan">医生排班</el-menu-item>
            <el-menu-item index="/nurse">护士管理</el-menu-item>
            <el-menu-item index="/nurseplan">护士排班</el-menu-item>
            <el-menu-item index="/user">患者管理</el-menu-item>
            <el-menu-item index="/ward">病房管理</el-menu-item>
            <el-menu-item index="/approval">住院审批</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
    };
  },
  created() {
    if (!this.user.id) {
      this.$router.push("/login");
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem("xm-user") || "{}"); // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === "ADMIN") {
        this.$router.push("/adminPerson");
      }
      if (this.user.role === "DOCTOR") {
        this.$router.push("/doctorPerson");
      }
      if (this.user.role === "USER") {
        this.$router.push("/userPerson");
      }
      if (this.user.role === "NURSE") {
        this.$router.push("/nursePerson");
      }
    },
    PersonalInformation() {
      this.$router.push("PersonalInformation");
    },
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
    register(){
      localStorage.removeItem("xm-user");
      this.$router.push("/register");
    },
    login(){
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
@import "@/assets/css/newCSS.css"; 
</style>
