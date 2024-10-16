<template>
  <div>
    <!-- 选择病房-->
    <el-select v-model="week" style="width: 300px" placeholder="请选择星期几">
      <el-option label="A号房" value="A号房"></el-option>
      <el-option label="B号房" value="B号房"></el-option>
      <el-option label="C号房" value="C号房"></el-option>
      <el-option label="D号房" value="D号房"></el-option>
    </el-select>

    <!--  输入姓名   -->
    <el-input v-model="input" style="width: 300px; margin-left: 10px" placeholder="请输入姓名"></el-input>

    <el-button type="warning" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
    <el-button type="primary" plain style="margin-left: 10px" @click="reset">重置</el-button>


    <!--      时间选择器-->
    <el-button style = "margin-left: 420px" @click="resetDateFilter">清除日期过滤器</el-button>
    <el-button @click="clearFilter">清除所有过滤器</el-button>

    <el-table
        ref="filterTable"
        :data="tableData"
        style="width: 100%">
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
          label="姓名">
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
          width="120">
        <template slot-scope="scope">
          <el-button
              @click.native.prevent="deleteRow(scope.$index, tableData)"
              type="text"
              size="big">
            备注
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
          prop="tag"
          label="标签"
          :filters="[{ text: '一日一次', value: '一日一次' }, { text: '一日两次', value: '一日两次' }]"
          :filter-method="filterTag"
          filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.tag === '一日一次' ? 'primary' : 'success'"
              disable-transitions>{{ scope.row.tag }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
export default {
  name: "DailyCare",
  methods: {
    handleClick(row) {
      console.log(row);
    },
    resetDateFilter() {
      this.$refs.filterTable.clearFilter('date');
    },
    clearFilter() {
      this.$refs.filterTable.clearFilter();
    },
    formatter(row, column) {
      return row.address;
    },
    filterTag(value, row) {
      return row.tag === value;
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    }
  },
  data() {
    return {
      input: '',
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: 'balabala',
        tag: '一日一次'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: 'balabala',
        tag: '一日两次'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: 'balabala',
        tag: '一日一次'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: 'balabala',
        tag: '一日两 次'
      }]
    }
  }
}
</script>


<style scoped>

</style>