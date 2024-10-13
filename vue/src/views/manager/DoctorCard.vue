<template xmlns="">
  <div>
    <div class="search">
      <el-select v-model="hospitalId" placeholder="请选择医院" >
        <div v-for="item in hospitalList">
          <el-option :label="item.hospitalName" :value="item.id"></el-option>
        </div>
      </el-select>

      <el-select v-model="departmentId" placeholder="请选择科室" style="width: 199px">
        <el-option v-for="item in planData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="selectedTime" placeholder="请选择时间" style="width: 199px">
        <el-option
            v-for="timeOption in timeOptions"
            :key="timeOption.value"
            :label="timeOption.label"
            :value="timeOption.value"
        >
        </el-option>
      </el-select>

      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <!-- doctorCard 显示区域 -->
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
      planData: [],
      hospitalList:[],
      timeOptions: [
        { label: '星期一', value: 1 },
        { label: '星期二', value: 2 },
        { label: '星期三', value: 3 },
        { label: '星期四', value: 4 },
        { label: '星期五', value: 5 },
        { label: '星期六', value: 6 },
        { label: '星期日', value: 7 },
      ],
    }
  },
  created() {
    this.load(1)
    this.loadPlan()
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
    /**
     * 查询所有计划，其 res.data 是一个数组，数组每一个元素都是一个 plan 表的元组
     * 组件属性 planData 将引用返回的数组（res.data）
     */
    loadPlan() {
      this.$request.get('/department/selectAll').then(res => {
        if (res.code === '200') {
          this.planData = res.data
          console.log("loadPlan: ")
          console.log(res)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    /**
     * 查询指定页的数据，并将返回数据保存在组件数据中
     * res.data 中的 total 是返回的总记录数，被组件属性 total 引用
     * res.data 中的 list 是包含医生个人信息的对象，被组件属性 tableData 引用
     * @param pageNum 页号
     */
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/doctor/selectPage2', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentId: this.departmentId,
          hospitalId:this.hospitalId,
        }
      }).then(res => {
        console.log("load(pageNum): ")
        console.log(res);
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