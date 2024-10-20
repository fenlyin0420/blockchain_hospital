<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png"/>
        <div class="title">基于SM2可链接环签名的智能区块链转诊系统</div>
      </div>

      <!-- 面包屑导航栏 -->
      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"/>
            <div style="color: #666666">{{ user.name || '管理员' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="PersonalInformation">密钥管理</el-dropdown-item>
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
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
          <!--default-openeds="['info','powerDoctor', 'reserve', 'user','powerUser']"-->
          <!--          <el-menu-item index="/home">-->
          <!--            <i class="el-icon-s-home"></i>-->
          <!--            <span slot="title" style="font-size: 20px;">系统首页</span>-->
          <!--          </el-menu-item>-->

          <!--   管理员   -->

          <el-submenu index="info" v-if="user.role === 'ADMIN'">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/notice">公告信息</el-menu-item>
            <el-menu-item index="/hospital">医院信息</el-menu-item>
            <el-menu-item index="/department">科室信息</el-menu-item>
            <el-menu-item index="/drug">药品信息</el-menu-item>
          </el-submenu>

          <el-submenu index="user" v-if="user.role === 'ADMIN'">
            <template slot="title">
              <i class="el-icon-menu"></i><span>用户管理</span>
            </template>
            <el-menu-item index="/doctor">医生管理</el-menu-item><!--
            <el-menu-item index="/plan">医生排班</el-menu-item>-->
            <el-menu-item index="/nurse">护士管理</el-menu-item><!--
            <el-menu-item index="/plan">护士排班</el-menu-item>-->
            <el-menu-item index="/user">患者管理</el-menu-item>
          </el-submenu>

          <el-submenu v-if="user.role === 'ADMIN'" index="/referralRecord">
            <template slot="title">
              <i class="el-icon-menu"></i><span>转诊管理</span>
            </template>
            <el-menu-item index="/referralRecord">转诊记录</el-menu-item>
            <el-menu-item index="/referralApplication">转诊申请</el-menu-item>
          </el-submenu>

          <!--   护士_护理任务     -->

          <el-submenu index="/nursecare" v-if="user.role === 'NURSE'">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>护理任务</span>
            </template>
            <el-menu-item index="/HealthCheck">健康检测</el-menu-item>
            <el-menu-item index="/AssignBeds">分配床位</el-menu-item>
            <el-menu-item index="/DailyCare">日常护理</el-menu-item>
          </el-submenu>

          <el-submenu index="info" v-if="user.role === 'NURSE'">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/plan">护士排班</el-menu-item>
          </el-submenu>

          <el-submenu index="powerDoctor" v-if="user.role === 'NURSE'">
            <template slot="title">
              <i class="el-icon-menu"></i><span>档案管理</span>
            </template>
            <el-menu-item index="/nurserecord">病床分配记录</el-menu-item>
          </el-submenu>

          <!--  患者   -->

          <div v-if="user.role === 'USER'">
            <el-menu-item index="/doctorCard"><i class="el-icon-menu"></i>预约挂号</el-menu-item>
            <el-menu-item index="/reserve" ><i class="el-icon-menu"></i>预约管理</el-menu-item>
            <el-menu-item index="/caseList"><i class="el-icon-menu"></i>溯源病历</el-menu-item>
          </div>

          <!--  医生 -->

          <el-submenu index="info" v-if="user.role === 'DOCTOR'">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/plan">医生排班信息</el-menu-item>
          </el-submenu>

          <el-submenu index="reserve" v-if="user.role === 'DOCTOR'">
            <template slot="title">
              <i class="el-icon-menu"></i><span>预约就诊</span>
            </template>
            <el-menu-item index="/reserve">患者挂号</el-menu-item>
            <el-menu-item index="/record">就诊记录</el-menu-item>
          </el-submenu>

          <el-submenu index="powerDoctor" v-if="user.role === 'DOCTOR' ">
            <template slot="title">
              <i class="el-icon-menu"></i><span>档案管理</span>
            </template>
            <div>
              <el-menu-item index="/caseList">病历列表</el-menu-item>
              <el-menu-item index="/case">新建病历</el-menu-item>
            </div>
          </el-submenu>

          <el-submenu index="record" v-if="user.role === 'DOCTOR'">
            <template slot="title">
              <i class="el-icon-menu"></i><span>转诊管理</span>
            </template>
            <el-menu-item index="/referralRecord">转诊记录</el-menu-item>
            <el-menu-item index="/referralApplication">转诊申请</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser"/>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/adminPerson')
      }
      if (this.user.role === 'DOCTOR') {
        this.$router.push('/doctorPerson')
      }
      if (this.user.role === 'USER') {
        this.$router.push('/userPerson')
      }
      if (this.user.role === 'NURSE') {
        this.$router.push('/nursePerson')
      }
    },
    PersonalInformation() {
      this.$router.push("PersonalInformation")
    },
    logout() {
      localStorage.removeItem('xm-user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/manager.css";

.el-icon-menu span {
  font-size: 30px;
}
</style>