/*大屏*/
//自调用函数
(function () {
    // 1、页面一加载就要知道页面宽度计算
    var setFont = function () {
        // 因为要定义变量可能和别的变量相互冲突，污染，所有用自调用函数
        var html = document.documentElement;// 获取html
        // 获取宽度
        var width = html.clientWidth;

        // 判断
        if (width < 1024) width = 1024
        if (width > 1920) width = 1920
        // 设置html的基准值
        var fontSize = width / 80 + 'px';
        // 设置给html
        html.style.fontSize = fontSize;
    }
    setFont();
    // 2、页面改变的时候也需要设置
    // 尺寸改变事件
    window.onresize = function () {
        setFont();
    }
})();

(function () {
    //事件委托
    $('.monitor').on('click', ' a', function () {
        //点击当前的a 加类名 active  他的兄弟删除类名
        $(this).addClass('active').siblings().removeClass('active');
        //获取一一对应的下标 
        var index = $(this).index();
        //选取content 然后狗日对应下标的 显示   当前的兄弟.content隐藏
        $('.content').eq(index).show().siblings('.content').hide();
    });
    //滚动
    //原理：把marquee下面的子盒子都复制一遍 加入到marquee中
    //      然后动画向上滚动，滚动到一半重新开始滚动
    //因为选取的是两个marquee  所以要遍历
    $('.monitor .marquee').each(function (index, dom) {
        //将每个 的所有子级都复制一遍
        var rows = $(dom).children().clone();
        //再将新的到的加入原来的
        $(dom).append(rows);
    });

})();
// 人名隔一个显示一个
// (function () {
//     // 中间省略的数据  准备三项
//     var item1 = {
//         name: '',
//         value: 30,
//         // 柱子颜色
//         itemStyle: {
//             color: '#254065'
//         },
//         // 鼠标经过柱子颜色
//         emphasis: {
//             itemStyle: {
//                 color: '#254065'
//             }
//         },
//         // 工具提示隐藏
//         tooltip: {
//             extraCssText: 'opacity:0'
//         }
//     };
//     var item2 = {
//         name: '',
//         value: 25,
//         // 柱子颜色
//         itemStyle: {
//             color: '#254065'
//         },
//         // 鼠标经过柱子颜色
//         emphasis: {
//             itemStyle: {
//                 color: '#254065'
//             }
//         },
//         // 工具提示隐藏
//         tooltip: {
//             extraCssText: 'opacity:0'
//         }
//     };
//     var item3 = {
//         name: '',
//         value: 20,
//         // 柱子颜色
//         itemStyle: {
//             color: '#254065'
//         },
//         // 鼠标经过柱子颜色
//         emphasis: {
//             itemStyle: {
//                 color: '#254065'
//             }
//         },
//         // 工具提示隐藏
//         tooltip: {
//             extraCssText: 'opacity:0'
//         }
//     };
//     option = {
//         // 工具提示
//         tooltip: {
//             // 触发类型  经过轴触发axis  经过轴触发item
//             trigger: 'item',
//             // 轴触发提示才有效
//             axisPointer: {
//                 // 默认为直线，可选为：'line' 线效果 | 'shadow' 阴影效果
//                 type: 'shadow'
//             }
//         },
//         // 图表边界控制
//         grid: {
//             // 距离 上右下左 的距离
//             left: '0',
//             right: '3%',
//             bottom: '3%',
//             top: '5%',
//             // 大小是否包含文本【类似于boxsizing】
//             containLabel: true,
//             //显示边框
//             show: true,
//             //边框颜色
//             borderColor: 'rgba(0, 240, 255, 0.3)'
//         },
//         // 控制x轴
//         xAxis: [
//             {
//                 // 使用类目，必须有data属性
//                 type: 'category',
//                 // 使用 data 中的数据设为刻度文字
//                 data: ['黄玉兰', '许国栋', '孙文杰', '李秀芬', '王建国',  '陈丽华', '周晓峰', '刘淑珍', '张明远'],
//                 // 刻度设置
//                 axisTick: {
//                     // true意思：图形在刻度中间
//                     // false意思：图形在刻度之间
//                     alignWithLabel: false,
//                     show: false
//                 },
//                 //文字
//                 axisLabel: {
//                     color: '#4c9bfd'
//                 }
//             }
//         ],
//         // 控制y轴
//         yAxis: [
//             {
//                 // 使用数据的值设为刻度文字
//                 type: 'value',
//                 axisTick: {
//                     // true意思：图形在刻度中间
//                     // false意思：图形在刻度之间
//                     alignWithLabel: false,
//                     show: false
//                 },
//                 //文字
//                 axisLabel: {
//                     color: '#4c9bfd'
//                 },
//                 splitLine: {
//                     lineStyle: {
//                         color: 'rgba(0, 240, 255, 0.3)'
//                     }
//                 },
//             }
//         ],
//         // 控制x轴
//         series: [
//
//             {
//                 // series配置
//                 // 颜色
//                 itemStyle: {
//                     // 提供的工具函数生成渐变颜色
//                     color: new echarts.graphic.LinearGradient(
//                         // (x1,y2) 点到点 (x2,y2) 之间进行渐变
//                         0, 0, 0, 1,
//                         [
//                             { offset: 0, color: '#00fffb' }, // 0 起始颜色
//                             { offset: 1, color: '#0061ce' }  // 1 结束颜色
//                         ]
//                     )
//                 },
//                 // 图表数据名称
//                 name: '用户统计',
//                 // 图表类型
//                 type: 'bar',
//                 // 柱子宽度
//                 barWidth: '60%',
//                 // 数据
//                 data: [62, 57, 41, 39, 37, 30, 25, 20, 10]
//             }
//         ]
//     };
//     var myechart = echarts.init($('.users .bar')[0]);
//     myechart.setOption(option);
// })();

// 人名完全显示
(function () {
    var item1 = {
        name: '',
        value: 30,
        itemStyle: {
            color: '#254065'
        },
        emphasis: {
            itemStyle: {
                color: '#254065'
            }
        },
        tooltip: {
            extraCssText: 'opacity:0'
        }
    };
    var item2 = {
        name: '',
        value: 25,
        itemStyle: {
            color: '#254065'
        },
        emphasis: {
            itemStyle: {
                color: '#254065'
            }
        },
        tooltip: {
            extraCssText: 'opacity:0'
        }
    };
    var item3 = {
        name: '',
        value: 20,
        itemStyle: {
            color: '#254065'
        },
        emphasis: {
            itemStyle: {
                color: '#254065'
            }
        },
        tooltip: {
            extraCssText: 'opacity:0'
        }
    };
    option = {
        tooltip: {
            trigger: 'item',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '0',
            right: '3%',
            bottom: '3%',
            top: '5%',
            containLabel: true,
            show: true,
            borderColor: 'rgba(0, 240, 255, 0.3)'
        },
        xAxis: [
            {
                type: 'category',
                data: ['黄玉兰', '许国栋', '孙文杰', '李秀芬', '王建国', '陈丽华', '周晓峰', '刘淑珍', '张明远'],
                axisTick: {
                    alignWithLabel: false,
                    show: false
                },
                axisLabel: {
                    color: '#4c9bfd',
                    interval: 0 // 确保每个标签都显示
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisTick: {
                    alignWithLabel: false,
                    show: false
                },
                axisLabel: {
                    color: '#4c9bfd'
                },
                splitLine: {
                    lineStyle: {
                        color: 'rgba(0, 240, 255, 0.3)'
                    }
                }
            }
        ],
        series: [
            {
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            { offset: 0, color: '#00fffb' },
                            { offset: 1, color: '#0061ce' }
                        ]
                    )
                },
                name: '用户统计',
                type: 'bar',
                barWidth: '60%',
                data: [62, 57, 41, 39, 37, 30, 25, 20, 10]
            }
        ]
    };
    var myechart = echarts.init($('.users .bar')[0]);
    myechart.setOption(option);
})();


(function () {
    // 使用原来饼图的容器，保持不变
    var myechart = echarts.init($('.pie')[0]);
    var option = {
        // 配置颜色（可按需要调整）
        color: ['#00c2ff', '#f9cf67', '#e92b77'],
        // 图例配置
        legend: {
            show: true,
            bottom: 0,
            center: 0,
            itemWidth: 14,
            itemHeight: 14,
            itemGap: 21,
            textStyle: {
                fontSize: 12,
                color: '#ade3ff'
            },
            data: ['临床能力', '科研水平', '服务质量']
        },
        // 雷达图坐标系配置
        radar: [{
            indicator: [
                { text: 'DRG达标率', max: 100 },
                { text: 'CMI指数', max: 100 },
                { text: '四级手术占比', max: 100 },
                { text: '论文影响因子', max: 100 },
                { text: '患者满意度', max: 100 }
            ],
            textStyle: { color: 'red' },
            center: ['50%', '50%'],
            radius: 60,
            startAngle: 90,
            splitNumber: 3,
            orient: 'horizontal',
            name: {
                formatter: '{value}',
                textStyle: {
                    fontSize: 12,
                    color: '#5b81cb'
                }
            },
            splitArea: {
                show: true,
                areaStyle: {
                    color: ['#141c42', '#141c42']
                }
            },
            axisLine: {
                lineStyle: { color: '#153269' }
            },
            splitLine: {
                lineStyle: {
                    color: '#113865',
                    width: 1
                }
            }
        }],
        // 将原来的饼图 series 改为雷达图数据配置
        series: [{
            // 保持系列名称不变，如“pie”
            name: 'pie',
            type: 'radar',
            itemStyle: {
                emphasis: {
                    lineStyle: { width: 4 }
                }
            },
            data: [
                {
                    name: '临床能力',
                    value: [95, 85, 92, 90, 92],
                    areaStyle: {
                        normal: {
                            color: {
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 1,
                                y2: 1,
                                colorStops: [
                                    { offset: 0, color: '#00c2ff' },
                                    { offset: 0.5, color: 'rgba(0,0,0,0)' },
                                    { offset: 1, color: '#00c2ff' }
                                ],
                                globalCoord: false
                            },
                            opacity: 1
                        }
                    },
                    symbolSize: 2.5,
                    label: {
                        normal: {
                            show: true,
                            position: 'top',
                            distance: 2,
                            color: '#6692e2',
                            fontSize: 14,
                            formatter: function(params) {
                                return params.value;
                            }
                        }
                    },
                    itemStyle: {
                        normal: {
                            borderColor: '#00c2ff',
                            borderWidth: 2.5
                        }
                    }
                },
                {
                    name: '科研水平',
                    value: [87, 86, 85, 75, 80],
                    symbolSize: 2.5,
                    itemStyle: {
                        normal: {
                            borderColor: '#f9cf67',
                            borderWidth: 2.5
                        }
                    },
                    areaStyle: {
                        normal: {
                            color: {
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 1,
                                y2: 1,
                                colorStops: [
                                    { offset: 0, color: '#f9cf67' },
                                    { offset: 0.5, color: 'rgba(0,0,0,0)' },
                                    { offset: 1, color: '#f9cf67' }
                                ],
                                globalCoord: false
                            },
                            opacity: 1
                        }
                    }
                },
                {
                    name: '服务质量',
                    value: [80, 70, 85, 75, 70],
                    symbolSize: 2.5,
                    itemStyle: {
                        normal: {
                            borderColor: '#e92b77',
                            borderWidth: 2.5
                        }
                    },
                    areaStyle: {
                        normal: {
                            color: {
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 1,
                                y2: 1,
                                colorStops: [
                                    { offset: 0, color: '#e92b77' },
                                    { offset: 0.5, color: 'rgba(0,0,0,0)' },
                                    { offset: 1, color: '#e92b77' }
                                ],
                                globalCoord: false
                            },
                            opacity: 1
                        }
                    }
                }
            ]
        }]
    };
    myechart.setOption(option);
})();

//订单
(function () {
    var data = {
        day365: { orders: '702', amount: '4,573' },
        day90: { orders: '229', amount: '1,307' },
        day30: { orders: '21', amount: '127' },
        day1: { orders: '1', amount: '5' }
    }
    //点击事件
    $('.order').on('click', '.filter a', function () {
        //点击之后加类名
        $(this).addClass('active').siblings().removeClass('active');
        // 先获取点击a的 data-key自定义属性
        var key = $(this).attr('data-key');
        //获取自定义属性
        // data{}==>data.shuxing data['shuxing]
        key = data[key];//
        $('.order .item h4:eq(0)').text(key.orders);
        $('.order .item h4:eq(1)').text(key.amount);
    });
    //定时器
    var index = 0;
    var aclick = $('.order a');
    setInterval(function () {
        index++;
        if (index > 3) {
            index = 0;
        }
        //每san秒调用点击事件
        aclick.eq(index).click();
    }, 3000);
})();
//销售
(function () {
    var option = {
        //鼠标提示工具
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            // 类目类型                                  
            type: 'category',
            // x轴刻度文字                                  
            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线  
            },
            boundaryGap: false//去除轴内间距
        },
        yAxis: {
            // 数据作为刻度文字                                  
            type: 'value',
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线  
            },
            boundaryGap: false//去除轴内间距
        },
        //图例组件
        legend: {
            textStyle: {
                color: '#4c9bfd' // 图例文字颜色

            },
            right: '10%'//距离右边10%
        },
        // 设置网格样式
        grid: {
            show: true,// 显示边框
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            borderColor: '#012f4a',// 边框颜色
            containLabel: true // 包含刻度文字在内
        },
        series: [{
            name: 'DRG权重标准费用',
            // 数据                                  
            data: [240, 400, 1010, 1340, 900, 2300, 2100, 2300, 1200, 2300, 2100, 1201],
            // 图表类型                                  
            type: 'line',
            // 圆滑连接                                  
            smooth: true,
            itemStyle: {
                color: '#00f2f1'  // 线颜色
            }
        },
        {
            name: '实际费用',
            // 数据                                  
            data: [400, 640, 1002, 1224, 790, 1930, 2040, 2130, 1800, 2000, 2800, 1590],
            // 图表类型                                  
            type: 'line',
            // 圆滑连接                                  
            smooth: true,
            itemStyle: {
                color: '#ed3f35'  // 线颜色
            }
        }]
    };
    var myechart = echarts.init($('.line')[0]);
    myechart.setOption(option);

    //点击效果
    var data = {
        year: [
            [240, 400, 1010, 1340, 900, 2300, 2100, 2300, 1200, 2300, 2100, 1201],
            [400, 640, 1002, 1224, 790, 1930, 2040, 2130, 1800, 2000, 2800, 1590]
        ],
    }
    $('.sales ').on('click', '.caption a', function () {
        $(this).addClass('active').siblings('a').removeClass('active');
        //option series   data
        //获取自定义属性值
        var key = $(this).attr('data-type');
        //取出对应的值
        key = data[key];
        //将值设置到 图表中
        option.series[0].data = key[0];
        option.series[1].data = key[1];
        //再次调用才能在页面显示
        myechart.setOption(option);
    });
    //定时器
    var index = 0;
    var timer = setInterval(function () {
        index++;
        if (index > 4) {
            index = 0;
        };
        $('.sales .caption a').eq(index).click();
    }, 2000);
})();
(function () {
    var option = {
        series: [
            {
                type: 'pie',
                radius: ['130%', '150%'],  // 放大图形
                center: ['50%', '80%'],    // 往下移动  套住75%文字
                label: {
                    show: false,
                },
                startAngle: 180,
                hoverOffset: 0,  // 鼠标经过不变大
                data: [
                    {
                        value: 100,
                        itemStyle: { // 颜色渐变#00c9e0->#005fc1
                            color: {
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 0,
                                y2: 1,
                                colorStops: [
                                    { offset: 0, color: '#00c9e0' },
                                    { offset: 1, color: '#005fc1' }
                                ]
                            }
                        }
                    },
                    { value: 100, itemStyle: { color: '#12274d' } }, // 颜色#12274d

                    { value: 200, itemStyle: { color: 'transparent' } }// 透明隐藏第三块区域
                ]
            }
        ]
    };
    var myechart = echarts.init($('.gauge')[0]);
    myechart.setOption(option);
})();

(function () {
    var data = [
        { name: '可爱多', num: '9,086' },
        { name: '娃哈哈', num: '8,341' },
        { name: '喜之郎', num: '7,407' },
        { name: '八喜', num: '6,080' },
        { name: '小洋人', num: '6,724' },
        { name: '好多鱼', num: '2,170' },
    ]
    $('.inner').on('mouseenter', '.sup li', function () {
        $(this).addClass('active').siblings().removeClass('active');
        //获取随机的值  sort方法 是给数组排序 a-b是从小到大
        //.5-随机0-1的数 可能为正可能为负 排序就会随机
        var radomData = data.sort(function (a, b) { return 0.5 - Math.random() });
        var html = '';
        radomData.forEach(function (item) {
            html += `<li><span>${item.name}</span><span>${item.num} <s class="icon-up"></s></span></li>`;
        });
        //渲染
        $('.sub').html(html);
    });
    $('.province .sup li').eq(0).mouseenter();
    var index = 0;
    var timer = setInterval(() => {
        index++;
        if (index > 5) {
            index = 0;
        }
        $('.sup li').eq(index).mouseenter();
    }, 2000);
})();