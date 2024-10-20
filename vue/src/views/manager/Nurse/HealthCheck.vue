<!--<template>-->
<!--  <div style="width: 100%">-->
<!--&lt;!&ndash;    <div id="pie1" style="width: 50%; height: 550px"></div>&ndash;&gt;-->
<!--    <div id="pie2" style="width: 100%; height: 1000px"></div>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import * as echarts from 'echarts';-->


<!--export default {-->
<!--  name: "HealthCheck",-->
<!--  data(){-->

<!--  },-->
<!--  methods:{-->
<!--    initPie() {-->
<!--      var chartDom = document.getElementById('pie1');-->
<!--      var myChart = echarts.init(chartDom);-->
<!--      var option;-->

<!--      option = {-->
<!--        title: {-->
<!--          text: '病房人数分布图',-->
<!--          left: 'center'-->
<!--        },-->
<!--        tooltip: {-->
<!--          trigger: 'item',-->
<!--          formatter: '{a} <br/>{b} : {c} ({d}%)'-->
<!--        },-->
<!--        legend: {-->
<!--          left: 'center',-->
<!--          top: 'bottom',-->
<!--          data: ['rose1', 'rose2', 'rose3', 'rose4']-->
<!--        },-->
<!--        toolbox: {-->
<!--          show: true,-->
<!--          feature: {-->
<!--            mark: {show: true},-->
<!--            dataView: {show: true, readOnly: false},-->
<!--            restore: {show: true},-->
<!--            saveAsImage: {show: true}-->
<!--          }-->
<!--        },-->
<!--        series: [-->
<!--          {-->
<!--            name: '病房号',-->
<!--            type: 'pie',-->
<!--            radius: [20, 140],-->
<!--            center: ['25%', '50%'],-->
<!--            roseType: 'radius',-->
<!--            itemStyle: {-->
<!--              borderRadius: 5-->
<!--            },-->
<!--            label: {-->
<!--              show: false-->
<!--            },-->
<!--            emphasis: {-->
<!--              label: {-->
<!--                show: true-->
<!--              }-->
<!--            },-->
<!--            data: [-->
<!--              {value: 40, name: 'A病房'},-->
<!--              {value: 33, name: 'B病房'},-->
<!--              {value: 28, name: 'C病房'},-->
<!--              {value: 22, name: 'D病房'}-->
<!--            ]-->
<!--          },-->
<!--          {-->
<!--            name: 'Area Mode',-->
<!--            type: 'pie',-->
<!--            radius: [20, 140],-->
<!--            center: ['75%', '50%'],-->
<!--            roseType: 'area',-->
<!--            itemStyle: {-->
<!--              borderRadius: 5-->
<!--            },-->
<!--            data: [-->
<!--              {value: 40, name: 'A病房'},-->
<!--              {value: 33, name: 'B病房'},-->
<!--              {value: 28, name: 'C病房'},-->
<!--              {value: 22, name: 'D病房'}-->
<!--            ]-->
<!--          }-->
<!--        ]-->
<!--      };-->

<!--      option && myChart.setOption(option);-->
<!--    },-->
<!--    initPie2(){-->
<!--      var chartDom = document.getElementById('pie2');-->
<!--      var myChart = echarts.init(chartDom);-->
<!--      var option;-->

<!--      const builderJson = {-->
<!--        all: 10887,-->
<!--        charts: {-->
<!--          A病房: 3237,-->
<!--          B病房: 2164,-->
<!--          C病房: 7561,-->
<!--          D病房: 7778-->
<!--        },-->
<!--        components: {-->
<!--          A病房: 2788,-->
<!--          B病房: 9575,-->
<!--          C病房: 9400,-->
<!--          D病房: 9466-->
<!--        },-->
<!--        ie: 9743-->
<!--      };-->
<!--      const downloadJson = {-->
<!--        男: 17365,-->
<!--        女: 14890-->
<!--      };-->
<!--      const themeJson = {-->
<!--        A型: 1594,-->
<!--        B型: 925,-->
<!--        AB型: 1608,-->
<!--        O型: 1982,-->
<!--        特殊血型: 2179-->
<!--      };-->
<!--      const waterMarkText = 'ECHARTS';-->
<!--      const canvas = document.createElement('canvas');-->
<!--      const ctx = canvas.getContext('2d');-->
<!--      canvas.width = canvas.height = 100;-->
<!--      ctx.textAlign = 'center';-->
<!--      ctx.textBaseline = 'middle';-->
<!--      ctx.globalAlpha = 0.08;-->
<!--      ctx.font = '20px Microsoft Yahei';-->
<!--      ctx.translate(50, 50);-->
<!--      ctx.rotate(-Math.PI / 4);-->
<!--      ctx.fillText(waterMarkText, 0, 0);-->
<!--      option = {-->
<!--        backgroundColor: {-->
<!--          type: 'pattern',-->
<!--          image: canvas,-->
<!--          repeat: 'repeat'-->
<!--        },-->
<!--        tooltip: {},-->
<!--        title: [-->
<!--          {-->
<!--            text: '在线统计',-->
<!--            subtext: '总计 ' + builderJson.all,-->
<!--            left: '25%',-->
<!--            textAlign: 'center'-->
<!--          },-->
<!--          {-->
<!--            text: '性别统计',-->
<!--            subtext:-->
<!--                '总计 ' +-->
<!--                Object.keys(downloadJson).reduce(function (all, key) {-->
<!--                  return all + downloadJson[key];-->
<!--                }, 0),-->
<!--            left: '75%',-->
<!--            textAlign: 'center'-->
<!--          },-->
<!--          {-->
<!--            text: '血型统计',-->
<!--            subtext:-->
<!--                '总计 ' +-->
<!--                Object.keys(themeJson).reduce(function (all, key) {-->
<!--                  return all + themeJson[key];-->
<!--                }, 0),-->
<!--            left: '75%',-->
<!--            top: '50%',-->
<!--            textAlign: 'center'-->
<!--          }-->
<!--        ],-->
<!--        grid: [-->
<!--          {-->
<!--            top: 50,-->
<!--            width: '50%',-->
<!--            bottom: '45%',-->
<!--            left: 10,-->
<!--            containLabel: true-->
<!--          },-->
<!--          {-->
<!--            top: '55%',-->
<!--            width: '50%',-->
<!--            bottom: 0,-->
<!--            left: 10,-->
<!--            containLabel: true-->
<!--          }-->
<!--        ],-->
<!--        xAxis: [-->
<!--          {-->
<!--            type: 'value',-->
<!--            max: builderJson.all,-->
<!--            splitLine: {-->
<!--              show: false-->
<!--            }-->
<!--          },-->
<!--          {-->
<!--            type: 'value',-->
<!--            max: builderJson.all,-->
<!--            gridIndex: 1,-->
<!--            splitLine: {-->
<!--              show: false-->
<!--            }-->
<!--          }-->
<!--        ],-->
<!--        yAxis: [-->
<!--          {-->
<!--            type: 'category',-->
<!--            data: Object.keys(builderJson.charts),-->
<!--            axisLabel: {-->
<!--              interval: 0,-->
<!--              rotate: 30-->
<!--            },-->
<!--            splitLine: {-->
<!--              show: false-->
<!--            }-->
<!--          },-->
<!--          {-->
<!--            gridIndex: 1,-->
<!--            type: 'category',-->
<!--            data: Object.keys(builderJson.components),-->
<!--            axisLabel: {-->
<!--              interval: 0,-->
<!--              rotate: 30-->
<!--            },-->
<!--            splitLine: {-->
<!--              show: false-->
<!--            }-->
<!--          }-->
<!--        ],-->
<!--        series: [-->
<!--          {-->
<!--            type: 'bar',-->
<!--            stack: 'chart',-->
<!--            z: 3,-->
<!--            label: {-->
<!--              position: 'right',-->
<!--              show: true-->
<!--            },-->
<!--            data: Object.keys(builderJson.charts).map(function (key) {-->
<!--              return builderJson.charts[key];-->
<!--            })-->
<!--          },-->
<!--          {-->
<!--            type: 'bar',-->
<!--            stack: 'chart',-->
<!--            silent: true,-->
<!--            itemStyle: {-->
<!--              color: '#eee'-->
<!--            },-->
<!--            data: Object.keys(builderJson.charts).map(function (key) {-->
<!--              return builderJson.all - builderJson.charts[key];-->
<!--            })-->
<!--          },-->
<!--          {-->
<!--            type: 'bar',-->
<!--            stack: 'component',-->
<!--            xAxisIndex: 1,-->
<!--            yAxisIndex: 1,-->
<!--            z: 3,-->
<!--            label: {-->
<!--              position: 'right',-->
<!--              show: true-->
<!--            },-->
<!--            data: Object.keys(builderJson.components).map(function (key) {-->
<!--              return builderJson.components[key];-->
<!--            })-->
<!--          },-->
<!--          {-->
<!--            type: 'bar',-->
<!--            stack: 'component',-->
<!--            silent: true,-->
<!--            xAxisIndex: 1,-->
<!--            yAxisIndex: 1,-->
<!--            itemStyle: {-->
<!--              color: '#eee'-->
<!--            },-->
<!--            data: Object.keys(builderJson.components).map(function (key) {-->
<!--              return builderJson.all - builderJson.components[key];-->
<!--            })-->
<!--          },-->
<!--          {-->
<!--            type: 'pie',-->
<!--            radius: [0, '30%'],-->
<!--            center: ['75%', '25%'],-->
<!--            data: Object.keys(downloadJson).map(function (key) {-->
<!--              return {-->
<!--                name: key.replace('.js', ''),-->
<!--                value: downloadJson[key]-->
<!--              };-->
<!--            })-->
<!--          },-->
<!--          {-->
<!--            type: 'pie',-->
<!--            radius: [0, '30%'],-->
<!--            center: ['75%', '75%'],-->
<!--            data: Object.keys(themeJson).map(function (key) {-->
<!--              return {-->
<!--                name: key.replace('.js', ''),-->
<!--                value: themeJson[key]-->
<!--              };-->
<!--            })-->
<!--          }-->
<!--        ]-->
<!--      };-->

<!--      option && myChart.setOption(option);-->
<!--    }-->
<!--  },-->
<!--  mounted() {-->
<!--    this.initPie();-->
<!--    this.initPie2();-->
<!--  }-->
<!--}-->
<!--</script>-->


<!--<style scoped>-->

<!--</style>-->