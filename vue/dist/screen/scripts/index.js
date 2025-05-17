var symptomName = last_month_day();

$(function () {
    init();
    init2();
    $("#el-dialog").addClass("hide");
    $(".close").click(function (event) {
        $("#el-dialog").addClass("hide");
    });

    var date = new Date();
    var numble = date.getDate();
    var today = getFormatMonth(new Date());
    $("#date1").html(today);
    $("#date2").html(today);
    $("#date3").html(today);
    $("#date4").html(today);

    lay('.demo-input').each(function () {
        laydate.render({
            type: 'month',
            elem: this,
            trigger: 'click',
            theme: '#95d7fb',
            calendar: true,
            showBottom: true,
            done: function () {
                console.log($("#startDate").val())
            }
        })
    });
})

function init() {
    // 计算包含所有点的地图中心
    function calculateMapCenter(points) {
        const lngs = points.map(p => p[0]);
        const lats = points.map(p => p[1]);
        return [
            (Math.max(...lngs) + Math.min(...lngs)) / 2,
            (Math.max(...lats) + Math.min(...lats)) / 2
        ];
    }

    // 主医院数据集
    const hospitalData = [
        {
            name: "XX大学第一附属医院",
            coords: [103.5901, 36.3043],
            color: '#3ed4ff'
        },
        {
            name: "XX大学第二附属医院",
            coords: [114.3896, 30.6628],
            color: '#ffa022'
        }
    ];

    // 提取医院坐标用于地图中心计算
    const hospitalPoints = hospitalData.map(item => item.coords);

    // 新增节点数据：连向第一附属医院
    const extraNodesGroup1 = [
        { name: '西宁医院', coords: [101.4038, 36.8207] },
        { name: '银川医院', coords: [106.3586, 38.1775] },
        { name: '成都医院', coords: [103.9526, 30.7617] },
        { name: '包头医院', coords: [110.3467, 41.4899] },
        { name: '乌鲁木齐医院', coords: [87.9236, 43.5883] }
    ];

    // 新增节点数据：连向第二附属医院
    const extraNodesGroup2 = [
        { name: '长沙医院', coords: [113.0823, 28.2568] },
        { name: '郑州医院', coords: [113.4668, 34.6234] },
        { name: '南昌医院', coords: [116.0046, 28.6633] },
        { name: '南京医院', coords: [118.8062, 31.9208] }
    ];

    // 初始化 ECharts 实例
    var mapChart = echarts.init(document.getElementById('mapChart'));

    mapChart.setOption({
        bmap: {
            center: calculateMapCenter(hospitalPoints),
            zoom: 5.5,
            roam: true,
            renderOnMoving: true,
            mapStyle: {
                style: 'midnight',
                features: [{
                    element: 'label',
                    stylers: { visibility: 'off' }
                }]
            }
        },
        series: [
            // 主医院节点（带涟漪效果）
            {
                type: 'effectScatter',
                coordinateSystem: 'bmap',
                rippleEffect: {
                    brushType: 'stroke',
                    scale: 4,
                    period: 3
                },
                symbolSize: 16,
                label: {
                    formatter: '{@[2]}',
                    position: 'right',
                    show: false
                },
                data: hospitalData.map(item => ({
                    value: item.coords.concat(item.name),
                    itemStyle: { color: item.color }
                }))
            },
            // 主医院之间的动态连线（圆形小球特效）
            {
                type: 'lines',
                coordinateSystem: 'bmap',
                zlevel: 2,
                effect: {
                    show: true,
                    period: 4,
                    trailLength: 0.7,
                    symbol: 'circle',
                    symbolSize: 10
                },
                lineStyle: {
                    color: {
                        type: 'linear',
                        x: 0, y: 0, x2: 1, y2: 0,
                        colorStops: [
                            { offset: 0, color: hospitalData[0].color },
                            { offset: 1, color: hospitalData[1].color }
                        ]
                    },
                    width: 1,
                    curveness: 0.3
                },
                data: [
                    { coords: [hospitalData[0].coords, hospitalData[1].coords] },
                    { coords: [hospitalData[1].coords, hospitalData[0].coords] }
                ]
            },
            // 连向第一附属医院的额外节点（不显示名称）
            {
                name: '额外节点组1',
                type: 'scatter',
                coordinateSystem: 'bmap',
                symbol: 'circle',
                symbolSize: 8,
                label: { show: false },
                itemStyle: { color: hospitalData[0].color },
                data: extraNodesGroup1.map(item => ({
                    value: item.coords
                }))
            },
            // 连向第二附属医院的额外节点（不显示名称）
            {
                name: '额外节点组2',
                type: 'scatter',
                coordinateSystem: 'bmap',
                symbol: 'circle',
                symbolSize: 8,
                label: { show: false },
                itemStyle: { color: hospitalData[1].color },
                data: extraNodesGroup2.map(item => ({
                    value: item.coords
                }))
            },
            // 连向第一附属医院的额外节点连线（带动态效果）
            {
                name: '连线组1',
                type: 'lines',
                coordinateSystem: 'bmap',
                zlevel: 2,
                effect: {
                    show: true,
                    period: 4,
                    trailLength: 0.7,
                    symbol: 'circle',
                    symbolSize: 10
                },
                lineStyle: {
                    color: hospitalData[0].color,
                    width: 1,
                    curveness: 0.2
                },
                data: extraNodesGroup1.map(item => ({
                    coords: [item.coords, hospitalData[0].coords]
                }))
            },
            // 连向第二附属医院的额外节点连线（带动态效果）
            {
                name: '连线组2',
                type: 'lines',
                coordinateSystem: 'bmap',
                zlevel: 2,
                effect: {
                    show: true,
                    period: 4,
                    trailLength: 0.7,
                    symbol: 'circle',
                    symbolSize: 10
                },
                lineStyle: {
                    color: hospitalData[1].color,
                    width: 1,
                    curveness: 0.2
                },
                data: extraNodesGroup2.map(item => ({
                    coords: [item.coords, hospitalData[1].coords]
                }))
            }
        ]
    });

    // 自适应调整
    setTimeout(() => mapChart.resize(), 300);
    window.addEventListener('resize', () => mapChart.resize());

    // 百度地图控件初始化（延时加载确保 bmap 实例已就绪）
    setTimeout(() => {
        const bmap = mapChart.getModel().getComponent('bmap').getBMap();
        bmap.addControl(new BMap.MapTypeControl({
            mapTypes: [BMAP_NORMAL_MAP, BMAP_SATELLITE_MAP]
        }));
    }, 1000);

    // 其他图表初始化
    var pieChart1 = echarts.init(document.getElementById('pieChart1'));
    pieChart1.setOption({
        color: ["#87cefa", "#ff7f50", "#32cd32", "#da70d6"],
        legend: {
            y: '260',
            x: 'center',
            textStyle: {
                color: '#ffffff',
            },
            data: ['内科', '外科', '儿科', '妇科'],
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a}<br/>{b}<br/>{c}G ({d}%)"
        },
        calculable: false,
        series: [
            {
                name: '采集数据量',
                type: 'pie',
                radius: ['40%', '70%'],
                center: ['50%', '45%'],
                itemStyle: {
                    normal: {
                        label: { show: false },
                        labelLine: { show: false }
                    },
                    emphasis: {
                        label: {
                            show: true,
                            position: 'center',
                            textStyle: {
                                fontSize: '20',
                                fontWeight: 'bold'
                            }
                        }
                    }
                },
                data: [
                    { value: 335, name: '内科' },
                    { value: 310, name: '外科' },
                    { value: 234, name: '儿科' },
                    { value: 135, name: '妇科' }
                ]
            }
        ]
    });

    var lineChart = echarts.init(document.getElementById('lineChart'));
    lineChart.setOption({
        color: ["#87cefa", "#ff7f50", "#32cd32", "#da70d6"],
        legend: {
            y: '260',
            x: 'center',
            textStyle: { color: '#ffffff' },
            data: ['内科', '外科', '儿科', '妇科'],
        },
        calculable: false,
        tooltip: {
            trigger: 'item',
            formatter: "{a}<br/>{b}<br/>{c}条"
        },
        yAxis: [
            {
                type: 'value',
                axisLine: { onZero: false, lineStyle: { color: '#034c6a' } },
                axisLabel: {
                    textStyle: { color: '#fff' },
                    formatter: function (value) { return value + "k条" },
                },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: ['8:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
                axisLine: { lineStyle: { color: '#034c6a' } },
                axisLabel: { textStyle: { color: '#fff' } },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        grid: { left: '5%', right: '5%', bottom: '20%', containLabel: true },
        series: [
            {
                name: '内科',
                type: 'line',
                smooth: true,
                itemStyle: {
                    normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } }
                },
                data: [0, 45, 15, 20, 60, 25, 22.1, 15, 10]
            },
            {
                name: '外科',
                type: 'line',
                smooth: true,
                itemStyle: {
                    normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } }
                },
                data: [10, 55, 25, 30, 70, 35, 32.1, 17, 12]
            },
            {
                name: '儿科',
                type: 'line',
                smooth: true,
                itemStyle: {
                    normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } }
                },
                data: [15, 65, 30, 40, 80, 45, 42.1, 20, 14]
            },
            {
                name: '妇科',
                type: 'line',
                smooth: true,
                itemStyle: {
                    normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } }
                },
                data: [20, 75, 40, 50, 90, 55, 52.1, 22, 16]
            }
        ]
    });

    var histogramChart = echarts.init(document.getElementById('histogramChart'));
    histogramChart.setOption({
        color: ["#87cefa", "#ff7f50", "#32cd32", "#da70d6"],
        legend: {
            y: '250',
            x: 'center',
            data: ['内科', '外科', '儿科', '妇科'],
            textStyle: { color: '#ffffff' }
        },
        calculable: false,
        grid: { left: '5%', right: '5%', bottom: '20%', containLabel: true },
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
        },
        xAxis: [
            {
                type: 'value',
                axisLabel: { textStyle: { color: '#fff' } },
                splitLine: { lineStyle: { color: ['#f2f2f2'], width: 0, type: 'solid' } }
            }
        ],
        yAxis: [
            {
                type: 'category',
                data: ['门诊人数(人)', '住院人次(人)', '人均费用(元)'],
                axisLabel: { textStyle: { color: '#fff' } },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        series: [
            {
                name: '内科',
                type: 'bar',
                stack: '总量',
                itemStyle: { normal: { label: { show: true, position: 'insideRight' } } },
                data: [320, 302, 301]
            },
            {
                name: '外科',
                type: 'bar',
                stack: '总量',
                itemStyle: { normal: { label: { show: true, position: 'insideRight' } } },
                data: [120, 132, 101]
            },
            {
                name: '儿科',
                type: 'bar',
                stack: '总量',
                itemStyle: { normal: { label: { show: true, position: 'insideRight' } } },
                data: [220, 182, 191]
            },
            {
                name: '妇科',
                type: 'bar',
                stack: '总量',
                itemStyle: { normal: { label: { show: true, position: 'insideRight' } } },
                data: [150, 212, 201]
            }
        ]
    });

    var lineChart2 = echarts.init(document.getElementById('lineChart2'));
    lineChart2.setOption({
        color: ["#87cefa", "#ff7f50", "#32cd32", "#da70d6"],
        legend: {
            y: '260',
            x: 'center',
            textStyle: { color: '#ffffff' },
            data: ['内科', '外科', '儿科', '妇科'],
        },
        calculable: false,
        tooltip: {
            trigger: 'item',
            formatter: "{a}<br/>{b}<br/>{c}条"
        },
        yAxis: [
            {
                type: 'value',
                axisLine: { onZero: false, lineStyle: { color: '#034c6a' } },
                axisLabel: {
                    textStyle: { color: '#fff' },
                    formatter: function (value) { return value + "k条" }
                },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: ['8:00', '10:00', '12:00', '14:00', '16:00', '18:00'],
                axisLine: { lineStyle: { color: '#034c6a' } },
                axisLabel: { textStyle: { color: '#fff' } },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        grid: { left: '5%', right: '5%', bottom: '20%', containLabel: true },
        series: [
            {
                name: '内科',
                type: 'line',
                smooth: true,
                itemStyle: { normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } } },
                data: [15, 0, 20, 45, 22.1, 25].reverse()
            },
            {
                name: '外科',
                type: 'line',
                smooth: true,
                itemStyle: { normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } } },
                data: [25, 10, 30, 55, 32.1, 35].reverse()
            },
            {
                name: '儿科',
                type: 'line',
                smooth: true,
                itemStyle: { normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } } },
                data: [35, 20, 40, 65, 42.1, 45].reverse()
            },
            {
                name: '妇科',
                type: 'line',
                smooth: true,
                itemStyle: { normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } } },
                data: [45, 30, 50, 75, 52.1, 55, 6].reverse()
            }
        ]
    });
}

function init2() {
    var lineChart3 = echarts.init(document.getElementById('lineChart3'));
    lineChart3.setOption({
        color: ["#87cefa", "#ff7f50"],
        legend: {
            y: 'top',
            x: 'center',
            textStyle: { color: '#ffffff' },
            data: ['门诊人次', '住院人次'],
        },
        calculable: false,
        tooltip: {
            trigger: 'item',
            formatter: "{a}<br/>{b}<br/>{c}人"
        },
        dataZoom: {
            show: true,
            realtime: true,
            start: 0,
            end: 18,
            height: 20,
            backgroundColor: '#f8f8f8',
            dataBackgroundColor: '#e4e4e4',
            fillerColor: '#87cefa',
            handleColor: '#87cefa',
        },
        yAxis: [
            {
                type: 'value',
                axisLine: { onZero: false, lineStyle: { color: '#034c6a' } },
                axisLabel: {
                    textStyle: { color: '#fff' },
                    formatter: function (value) { return value + "人" }
                },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: symptomName,
                boundaryGap: false,
                axisLine: { lineStyle: { color: '#034c6a' } },
                axisLabel: { textStyle: { color: '#fff' } },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        grid: { left: '5%', right: '5%', bottom: '20%', containLabel: true },
        series: [
            {
                name: '门诊费用',
                type: 'line',
                smooth: true,
                itemStyle: { normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } } },
                data: [1150, 180, 2100, 2415, 1212.1, 3125, 1510, 810, 2100, 2415, 1122.1, 3215, 1510, 801, 2001, 2245, 1232.1, 3245, 1520, 830, 2200, 2145, 1223.1, 325, 150, 80, 200, 245, 122.1, 325]
            },
            {
                name: '住院费用',
                type: 'line',
                smooth: true,
                itemStyle: { normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } } },
                data: [2500, 1000, 3000, 5005, 3200.1, 3005, 2500, 1000, 3000, 5005, 3200.1, 3005, 2500, 1000, 3000, 5005, 3200.1, 3005, 2500, 1000, 3000, 5005, 3200.1, 3005, 2500, 1000, 3000, 5005, 3200.1, 3005, 2500, 1000, 3000, 5005, 3200.1, 3005,]
            },
        ]
    });

    var lineChart4 = echarts.init(document.getElementById('lineChart4'));
    lineChart4.setOption({
        color: ["#87cefa", "#ff7f50"],
        calculable: false,
        tooltip: {
            trigger: 'item',
            formatter: "{a}<br/>{b}<br/>{c}元"
        },
        dataZoom: {
            show: true,
            realtime: true,
            start: 0,
            end: 18,
            height: 20,
            backgroundColor: '#f8f8f8',
            dataBackgroundColor: '#e4e4e4',
            fillerColor: '#87cefa',
            handleColor: '#87cefa',
        },
        yAxis: [
            {
                type: 'value',
                axisLine: { onZero: false, lineStyle: { color: '#034c6a' } },
                axisLabel: {
                    textStyle: { color: '#fff' },
                    formatter: function (value) { return value + "元" }
                },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: symptomName,
                boundaryGap: false,
                axisLine: { lineStyle: { color: '#034c6a' } },
                axisLabel: { textStyle: { color: '#fff' } },
                splitLine: { lineStyle: { width: 0, type: 'solid' } }
            }
        ],
        grid: { left: '5%', right: '5%', bottom: '20%', containLabel: true },
        series: [
            {
                name: '医疗费用',
                type: 'line',
                smooth: true,
                itemStyle: { normal: { lineStyle: { shadowColor: 'rgba(0,0,0,0.4)' } } },
                data: [1500, 800, 1200, 2450, 1122.1, 1325, 1150, 180, 1200, 1245, 1122.1, 1325, 150, 180, 1200, 2145, 1212.1, 3215, 1510, 180, 2100, 2415, 122.1, 325, 150, 80, 200, 245, 122.1, 325].reverse()
            },
        ]
    });

    // 年龄分布
    var pieChart2 = echarts.init(document.getElementById('pieChart2'));
    pieChart2.setOption({
        color: ["#32cd32", "#ff7f50", "#87cefa", "#FD6C88", "#4b5cc4", "#faff72"],
        tooltip: {
            trigger: 'item',
            formatter: "{a}<br/>{b}<br/>{c}人"
        },
        calculable: true,
        series: [
            {
                name: '发病人数',
                type: 'pie',
                radius: [30, 110],
                center: ['50%', '50%'],
                roseType: 'area',
                sort: 'ascending',
                data: [
                    { value: 10, name: '婴儿(1-3岁)' },
                    { value: 5, name: '少儿(4-10岁)' },
                    { value: 15, name: '少年(10-18岁)' },
                    { value: 25, name: '青年(18-45岁)' },
                    { value: 125, name: '中年(45-60岁)' },
                    { value: 175, name: '老年(60岁以上)' }
                ]
            }
        ]
    });

    // 医疗费用组成
    var pieChart3 = echarts.init(document.getElementById('pieChart3'));
    pieChart3.setOption({
        color: ["#32cd32", "#ff7f50", "#87cefa", "#FD6C88", "#4b5cc4", "#faff72"],
        tooltip: {
            trigger: 'item',
            formatter: "{a}<br/>{b}<br/>{c}元"
        },
        calculable: true,
        series: [
            {
                name: '发病人数',
                type: 'pie',
                radius: [30, 110],
                center: ['50%', '50%'],
                roseType: 'area',
                sort: 'ascending',
                data: [
                    { value: 10, name: '诊察费用' },
                    { value: 500, name: '检查费用' },
                    { value: 150, name: '检验费用' },
                    { value: 250, name: '西药费用' },
                    { value: 125, name: '中药费用' },
                    { value: 1750, name: '手术费用' }
                ]
            }
        ]
    })
}
