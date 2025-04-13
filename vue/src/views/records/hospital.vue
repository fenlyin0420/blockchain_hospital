<template>
  <div class="body">
    <!-- <div class="search">
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="title"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>

      <el-button type="primary" plain @click="handleAdd">新增</el-button>
    </div>
    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column prop="hospitalName" label="医院名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="url" label="网址" show-overflow-tooltip></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="del(scope.row.id)">删除</el-button>
            <el-button plain type="primary" @click="update(scope.row)">修改</el-button>
          </template>
</el-table-column>
</el-table>

<div class="pagination">
  <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum" :page-sizes="[5, 10, 20]"
    :page-size="pageSize" layout="total, prev, pager, next" :total="total">
  </el-pagination>
</div> -->

    <!-- <img
        src="../../assets/imgs/qj.png" 
        width="100%"
        height="100%"
      ></img> -->

    <iframe src="http://localhost:8080/screen/views/index.html" class="window"></iframe>
    <!-- </div>


    <el-dialog title="医院添加" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="num" label="医院名称">
          <el-input v-model="form.hospitalName" autocomplete="off" placeholder="请输入医院名称"></el-input>
        </el-form-item>
        <el-form-item prop="num" label="医院url">
          <el-input v-model="form.url" autocomplete="off" placeholder="请输入医院url"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog> -->

  </div>
</template>

<script>

export default {
  name: "Record",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        inHospital: [
          { required: true, message: '请选择是否住院', trigger: 'blur' },
        ],
      },
      ids: [],
      viewContent: null,
      editorVisible: false,
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$request.post('/hospital/update', this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    update(data) {
      this.form = data;
      this.fromVisible = true
    },
    handleAdd() {
      this.form = {};
      this.fromVisible = true
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', { type: "warning" }).then(response => {
        this.$request.delete('/hospital/delete/' + id).then(res => {
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
      this.$request.get('/hospital/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleSelectionChange() { }, //批量
  }
}
</script>

<style scoped>
.body {
  height: 100vh;
  margin: 0;
  overflow: hidden;
}

.window {
  width: 100%;
  height: 100%;
  border: none;
}
</style>
