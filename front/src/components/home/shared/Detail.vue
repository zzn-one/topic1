<template>
    <div>
        <table class="dia" align="center" width="900px" border="2px" cellspacing="0" cellpadding="10" ref="checkTable">
            <caption>
                <h1>毕业设计选题审核表</h1>
            </caption>
            <tr>
                <td width="150px">指导老师姓名</td>
                <td width="100px">{{ tutor.tName }}</td>
                <td width="70px">职称</td>
                <td width="370px">{{ tutor.title }}</td>
                <td width="150px">指导学生数量</td>
                <td width="120px">{{ tutor.stuNum }}</td>
            </tr>
            <tr>
                <td></td>
                <td colspan="2">课题名称</td>
                <td>命题性质</td>
                <td>来源</td>
                <td>是否合适</td>
            </tr>
            <template>
                <tr v-for="item, index in topicList">
                    <td width="40px">{{ index + 1 }}</td>
                    <td colspan="2" width="500px" align="left">{{ item.topicName }}</td>
                    <td width="120px">{{ item.type }}</td>
                    <td width="120px">{{ item.source }}</td>
                    <td width="120px">{{ item.passed }}</td>
                </tr>
            </template>
            <tr>
                <td width="120px" rowspan="3" height="170px">存在问题及其需要整改的建议</td>
                <td rowspan="3" colspan="3" width="540px" align="left" v-html="suggestions" class="suggestions_td">

                </td>
                <td width="120px">不合格课题数</td>
                <td width="120px">{{ passedResult.unPassed }}</td>
            </tr>
            <tr>
                <td width="120px">基本合格数</td>
                <td width="120px">{{ passedResult.basePassed }}</td>
            </tr>
            <tr>
                <td width="120px">合格课题数</td>
                <td width="120px">{{ passedResult.passed }}</td>
            </tr>
        </table>

        <el-button plain type="primary" @click="pdfDownload">导出审核表</el-button>
    </div>
</template>
<script>
import html2canvas from 'html2canvas'
import JsPDF from 'jspdf'

export default {
    name: "Detail",
    data() {
        return {
            tutor: {
                tName: '',
                title: '',
                stuNum: '',
            },
            topicList: [],
            passedResult: {
                unPassed: 0,
                basePassed: 0,
                passed: 0
            },
            suggestions: ''

        }
    },
    methods: {
        //导出审核表
        pdfDownload() {
            let myBox = this.$refs.checkTable; //获取ref里面的内容
            html2canvas(myBox, {
                useCORS: true, //是否尝试使用CORS从服务器加载图像
                allowTaint: true,
                dpi: 300, //解决生产图片模糊
                scale: 3, //清晰度--放大倍数
            }).then(function (canvas) {
                let contentWidth = canvas.width
                let contentHeight = canvas.height
                let pageHeight = contentWidth / 592.28 * 841.89 // 一页pdf显示html页面生成的canvas高度;
                let leftHeight = contentHeight //未生成pdf的html页面高度
                let position = 0 //pdf页面偏移
                //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
                // let imgWidth = 595.28
                let imgWidth = 560.28  //宽度
                let imgHeight = 592.28 / contentWidth * contentHeight
                let pageData = canvas.toDataURL('image/jpeg', 1.0)
                let PDF = new JsPDF('', 'pt', 'a4')

                // 有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
                //当内容未超过pdf一页显示的范围，无需分页
                if (leftHeight < pageHeight) {
                    PDF.addImage(pageData, 'JPEG', 20, 20, imgWidth, imgHeight)
                } else {
                    while (leftHeight > 0) {
                        PDF.addImage(pageData, 'JPEG', 20, position, imgWidth, imgHeight)
                        leftHeight -= pageHeight
                        position -= 841.89
                        if (leftHeight > 0) {
                            PDF.addPage()
                        }
                    }
                }
                PDF.save('课题审核表.pdf')//下载标题
            });
        },
    },
    created() {
        //根据导师用户id 获取 导师姓名、职称、课题数量（已被选择的课题）
        this.tutor.tName = "江婷"
        this.tutor.title = '副教授'
        this.tutor.stuNum = 9

        //根据导师id获取该导师的所有课题（课题名称、命题性质、来源、审核结果）
        this.topicList = []

        for (let index = 0; index < 10; index++) {
            this.topicList.push(
                {
                    topicName: '基于xxx的xxx管理系统',
                    type: '设计',
                    source: '自命题',
                    passed: '及格',
                    suggestion: '建议dsdasdsad' + index
                }
            )
        }


        //遍历topicList，统计出passedResult（unPassed,basePassed, passed）
        const _this = this
        this.topicList.forEach((item) => {
            if (item.passed === "及格") {
                _this.passedResult.passed++
            } else if (item.passed === "基本及格") {
                _this.passedResult.basePassed++
            } else if (item.passed === "不及格") {
                _this.passedResult.unPassed++
            }
        })
        //遍历topicList,合并修改建议
        this.topicList.forEach((item, index) => {
            _this.suggestions +=
                "课题"
                + (index + 1)
                + ":"
                + item.suggestion
                + "<br>"
        })

    }


}
</script>
<style scoped lang="less">
.CheckTable {
    border: 2px solid;
}

table {
    border-collapse: collapse;
    margin: 0 auto;
}

.dia {
    line-height: 23px;
    font-size: 18px;
}

.dia td {
    font-size: 15px;
    text-align: center;
}
</style>
  