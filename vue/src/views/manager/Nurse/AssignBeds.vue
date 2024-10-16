<template>
  <div>
    <!--   嵌套表单   -->
    <!--    <el-button type="primary" round @click="dialogFormVisible = true">点击分配床位</el-button>-->

    <!--    <el-dialog title="分配床位" :visible.sync="dialogFormVisible">-->
    <!--      <el-form ref="form" :model="form" label-width="80px">-->
    <!--        <el-form-item label="患者名称">-->
    <!--          <el-input v-model="form.name"></el-input>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="病房号">-->
    <!--          <el-select v-model="form.region" placeholder="请选择病房号">-->
    <!--            <el-option label="病房A" value="1"></el-option>-->
    <!--            <el-option label="病房B" value="2"></el-option>-->
    <!--            <el-option label="病房C" value="3"></el-option>-->
    <!--            <el-option label="病房D" value="4"></el-option>-->
    <!--          </el-select>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="住院时间">-->
    <!--          <el-col :span="11">-->
    <!--            <el-date-picker type="date" placeholder="选择日期" v-model="form.date1" style="width: 100%;"></el-date-picker>-->
    <!--          </el-col>-->
    <!--          <el-col class="line" :span="2">-</el-col>-->
    <!--          <el-col :span="11">-->
    <!--            <el-time-picker placeholder="选择时间" v-model="form.date2" style="width: 100%;"></el-time-picker>-->
    <!--          </el-col>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="VIP">-->
    <!--          <el-switch v-model="form.delivery"></el-switch>-->
    <!--        </el-form-item>-->

    <!--        <el-form-item label="特殊资源">-->
    <!--          <el-radio-group v-model="form.resource">-->
    <!--            <el-radio label="单人病房"></el-radio>-->
    <!--            <el-radio label="多人病房"></el-radio>-->
    <!--          </el-radio-group>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="备注">-->
    <!--          <el-input type="textarea" v-model="form.desc"></el-input>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item>-->
    <!--          <el-button type="primary" @click="onSubmit">立即创建</el-button>-->
    <!--          <el-button>取消</el-button>-->
    <!--        </el-form-item>-->
    <!--      </el-form>-->
    <!--    </el-dialog>-->

    <!--    床位可视化_日历  -->
<!--    <el-calendar v-model="value">-->
<!--    </el-calendar>-->

<!--    模拟床位    -->
<!--    <el-row style="margin-top:22px; margin-left: 15px">-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--    </el-row>-->

<!--    <el-row style="margin-top: 10px; margin-left: 15px">-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--    </el-row>-->

<!--    <el-row style="margin-top: 10px; margin-left: 15px">-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="primary" round>空位</el-button>-->
<!--      <el-button type="danger" round>已满</el-button>-->
<!--    </el-row>-->

    <el-table
        :data="tableData"
        style="width: 100%">
      <el-table-column
          label="日期"
          width="180">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.date }}</span>
        </template>

      </el-table-column>
      <el-table-column label="病房A">
<!--        <template slot-scope="scope">-->
          <el-button type="primary" size="mini" round>空位</el-button>
<!--        </template>-->
      </el-table-column>

      <el-table-column label="病房B">
        <!--        <template slot-scope="scope">-->
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <!--        </template>-->
      </el-table-column>

      <el-table-column label="病房C">
        <!--        <template slot-scope="scope">-->
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <!--        </template>-->
      </el-table-column>

      <el-table-column label="病房D">
        <!--        <template slot-scope="scope">-->
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <!--        </template>-->
      </el-table-column>
    </el-table>

<!--    <el-form :inline="true" :model="searchForm" class="demo-form-inline" style="margin-top: 20px">-->
<!--      <el-form-item label="姓名">-->
<!--        <el-input v-model="searchForm.name" placeholder="姓名"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="病房号">-->
<!--        <el-select v-model="searchForm.region" placeholder="病房号">-->
<!--          <el-option label="病房A" value="1"></el-option>-->
<!--          <el-option label="病房B" value="2"></el-option>-->
<!--          <el-option label="病房C" value="3"></el-option>-->
<!--          <el-option label="病房D" value="4"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" @click="onSubmit">查询</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->

    <el-table
        ref="filterTable"
        :data="tableData"
        style="width: 100%; margin-top: 20px">
      <el-table-column
          prop="date"
          label="日期"
          sortable
          column-key="date"
          :filters="[{text: '2016-05-01', value: '2016-05-01'}, {text: '2016-05-02', value: '2016-05-02'}, {text: '2016-05-03', value: '2016-05-03'}, {text: '2016-05-04', value: '2016-05-04'}]"
          :filter-method="filterHandler"
      >
      </el-table-column>
      <el-table-column
          prop="name"
          label="患者">
      </el-table-column>
      <el-table-column
          prop="doctorName"
          label="医生姓名">
      </el-table-column>
      <el-table-column
          prop="address"
          label="处方"
          :formatter="formatter">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="primary" size="small">查看</el-button>
          <el-button type="danger" size="small">编辑</el-button>
        </template>
      </el-table-column>
      <el-table-column
          prop="tag"
          label="标签"
          :filters="[{ text: '单人病房', value: '单人病房' }, { text: '双人病房', value: '双人病房' }]"
          :filter-method="filterTag"
          filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.tag === '单人病房' ? 'primary' : 'success'"
              disable-transitions>{{ scope.row.tag }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>


  </div>
</template>

<script>
export default {
  name: "AssignBeds",
  data() {
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: 'balabala',
        tag: '单人病房'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: 'balabala',
        tag: '双人病房'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: 'balabala',
        tag: '单人病房'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: 'balabala',
        tag: '双人病房'
      }],
      dialogFormVisible: false,
      searchForm: {
        name: '',
        region: ''
      },
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      value: new Date()
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!');
    },
    handleClick(row) {
      console.log(row);
    }
  }
}
</script>

<style scoped>

</style>