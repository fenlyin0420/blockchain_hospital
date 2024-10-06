<template xmlns="">
  <div>
    <div class="search">
      <el-select v-model="hospitalId" placeholder="请选择医院" >
        <div v-for="item in hospitalList">
          <el-option :label="item.hospitalName" :value="item.id"></el-option>
        </div>
      </el-select>

      <el-select v-model="departmentId" placeholder="请选择科室" style="width: 199px">
        <el-option v-for="item in departmentData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="departmentId" placeholder="请选择时间" style="width: 199px">
        <el-option v-for="item in departmentData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>

      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table" style="padding: 15px 20px">
      <el-row :gutter="20">

        <el-col :span="6" v-for="item in tableData" style="margin-bottom: 20px">
          <div style="text-align: center; background-color: #ecf8fd" class="card">
            <img :src="item.avatar" alt="" style="width: 100px; height: 100px; border-radius: 50%">
            <div style="font-weight: 550; margin-top: 10px">
              医院 <span style="color: #383535; margin-left: 5px; font-weight: 500">{{item.hospitalName}}</span>
            </div>
            <div style="font-weight: 550; margin-top: 10px">
              {{item.name}} <span style="color: #383535; margin-left: 5px; font-weight: 500">{{item.departmentName}}</span>
            </div>
            <div style="margin-top: 20px; color: #353523; padding: 0 10px; text-align: left; overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;">
              简介：{{item.description}}
            </div>
            <div style="margin-top: 15px">
              挂号费：<span style="color: red; font-weight: 550; margin-right: 20px">￥{{item.price}}</span> 剩余：{{item.num}}
            </div>
            <div style="margin-top: 15px">
              <el-button type="primary" size="mini" @click="reserve(item.id,item.hospitalId)">挂号</el-button>
            </div>
          </div>
        </el-col>
      </el-row>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Doctor",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      departmentId: null,
      hospitalId:null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: [],
      departmentData: [],
      hospitalList:[]
    }
  },
  created() {
    this.load(1)
    this.loadDepartment()
    this.load2()
  },
  methods: {
    reserve(doctorId,hospitalId) {
      if (this.user.role !== 'USER') {
        this.$message.warning('您的角色不支持挂号操作')
        return
      }
      let data = {
        userId: this.user.id,
        doctorId: doctorId,
        hospitalId: hospitalId
      }
      this.$request.post('/reserve/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('挂号成功')
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadDepartment() {
      this.$request.get('/department/selectAll').then(res => {
        if (res.code === '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      // debug
      // window.alert("查询第" + pageNum + "页");

      if (pageNum) this.pageNum = pageNum
      this.$request.get('/doctor/selectPage2', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentId: this.departmentId,
          hospitalId:this.hospitalId,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    load2(){
      this.$request.get('/hospital/selectAll').then(res=>{
        this.hospitalList=res.data
      })
    },
    reset() {
      this.departmentId = null;
      this.hospitalId = null;
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>
.table {
  font-size: 16px;
}
</style>