<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入药品id" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
    </div>
    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="id" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="hospitalName" label="医院" show-overflow-tooltip></el-table-column>
        <el-table-column prop="drugNumber" label="生产批号" show-overflow-tooltip></el-table-column>
        <el-table-column prop="drugName" label="药名"></el-table-column>
        <el-table-column prop="price" label="价格"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <el-dialog title="药品添加" :visible.sync="formVisible" width="60%" :close-on-click-modal="false" destroy-on-close @close="cancel">
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="drugNumber" label="生产批号">
          <el-input v-model="form.drugNumber" autocomplete="off" placeholder="请输入生产批号"></el-input>
        </el-form-item>
        <el-form-item prop="drugName" label="药名">
          <el-input v-model="form.drugName" autocomplete="off" placeholder="请输入药名"></el-input>
        </el-form-item>
        <el-form-item prop="price" label="价格">
          <el-input v-model="form.price" autocomplete="off" placeholder="请输入价格"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Drug",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      status: null,
      formVisible: false,
      form: {
        hospitalId: '',
        drugNumber: '',
        drugName: '',
        price: ''
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        drugNumber: [
          { required: true, message: '请输入生产批号', trigger: 'blur' }
        ],
        drugName: [
          { required: true, message: '请输入药名', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' }
        ]
      },
      hospitalList:[]
    }
  },
  created() {
    this.load(1)
    this.load2()
  },
  methods: {
    call(row) {
      let reserveData = { ...row, status: '已叫号' };
      this.$request.put('/drug/update', reserveData).then(res => {
        if (res.code === '200') {
          this.$message.success('叫号成功')
          this.load(1)
          this.record(row)
        }
      })
    },
    record(row) {
      let data = {
        userId: row.userId,
        doctorId: row.doctorId,
      }
      this.$request.post('/record/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('数据同步成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.$confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', {type: "warning"}).then(response => {
        this.$request.delete('/drug/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.formVisible = true   // 打开弹窗
    },
    save() {
      this.$request.post('/drug/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.load(1)
          this.formVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.form = {
        hospitalId: this.user.hospitalId,
        drugNumber: '',
        drugName: '',
        price: ''
      }
      this.formVisible = true
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/drug/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.status,
          hospitalId:this.user.hospitalId
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
      this.status = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
