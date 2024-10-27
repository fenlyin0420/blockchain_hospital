<template>
  <div>
    <div class="search">
      <!--  输入姓名   -->
      <el-input v-model="input" style = "width: 300px" placeholder="请输入姓名"></el-input>
      <!-- 下拉框选择星期几 -->
      <el-select v-model="week" style = "width: 200px; margin-left: 10px" placeholder="请选择星期几" >
        <el-option label="星期一" value="星期一"></el-option>
        <el-option label="星期二" value="星期二"></el-option>
        <el-option label="星期三" value="星期三"></el-option>
        <el-option label="星期四" value="星期四"></el-option>
        <el-option label="星期五" value="星期五"></el-option>
        <el-option label="星期六" value="星期六"></el-option>
        <el-option label="星期日" value="星期日"></el-option>
      </el-select>

      <!--      时间选择器-->

      <el-button type="warning" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="primary" plain style="margin-left: 10px" @click="reset">重置</el-button>

      <el-button type="primary" plain style="margin-left: 620px" @click="handleAdd">新增</el-button>
      <el-button type="danger" plain style="margin-left: 10px" @click="delBatch">批量删除</el-button>
    </div>

<!--    <div class="operation">-->
<!--      <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
<!--      <el-button type="danger" plain @click="delBatch">批量删除</el-button>-->
<!--    </div>-->

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="doctorName" label="护士姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="病房" show-overflow-tooltip></el-table-column>
        <el-table-column prop="hospitalName" label="医院"></el-table-column>
        <el-table-column prop="num" label="就诊数量"></el-table-column>
        <el-table-column prop="week" label="周几"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini" v-if="scope.row.hospitalId===user.hospitalId">编辑</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id) v-if="scope.row.hospitalId===user.hospitalId">删除</el-button>
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


    <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="doctorId" label="选择医生">
          <el-select v-model="form.doctorId" placeholder="请选择医生" style="width: 100%">
            <el-option v-for="item in doctorData" :key="item.id" :label="item.name + ' - ' + item.departmentName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="num" label="看病人数">
          <el-input v-model="form.num" autocomplete="off" placeholder="请输入看病人数"></el-input>
        </el-form-item>
        <el-form-item prop="week" label="选择周几">
          <el-select v-model="form.week" placeholder="请选择周几" style="width: 100%">
            <el-option label="星期一" value="星期一"></el-option>
            <el-option label="星期二" value="星期二"></el-option>
            <el-option label="星期三" value="星期三"></el-option>
            <el-option label="星期四" value="星期四"></el-option>
            <el-option label="星期五" value="星期五"></el-option>
            <el-option label="星期六" value="星期六"></el-option>
            <el-option label="星期日" value="星期日"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "Plan",
  data() {
    return {
      input:'',
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      week: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        doctorId: [
          {required: true, message: '请选择医生', trigger: 'blur'},
        ],
        num: [
          {required: true, message: '请输入人数', trigger: 'blur'},
        ],
        week: [
          {required: true, message: '请选择周几', trigger: 'blur'},
        ]
      },
      ids: [],
      doctorData: [],
    }
  },
  created() {
    this.load(1)
    // this.loadDoctor()
    console.log("组件已创建");
  },
  methods: {
    loadDoctor() {
      //debug
      console.log("this.user", this.user);
      let id=this.user.hospitalId // 各医院管理员只能管理自己医院的排班，所以是selectById
      this.$request.get('/nurse/selectByH/'+id).then(res => {
        //this.$request.get('/doctor/selectAll/').then(res => { // 加载所有医生，而不是按照医院id加载
        if (res.code === '200') {
          this.doctorData = res.data
          // debug
          console.log(res);
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.form.hospitalId=this.user.hospitalId
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/plan/update' : '/plan/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/plan/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/plan/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/plan/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          week: this.week,
          hospitalId:this.user.hospitalId
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.week = null
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
