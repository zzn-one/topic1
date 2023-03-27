<template>
    <el-card class="box-card2">
        <el-form inline :model="exportForm">
            <el-form-item label="专业">
                <el-select v-model="exportForm.major">
                    <el-option v-for="item in majorList" :label="item.description" :value="item.major"
                        :key="item.major">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="毕业年份">
                <el-select v-model="exportForm.year">
                    <el-option v-for="year in yearList" :label="year" :value="year" :key="year">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item :label="'查询信息数目(当前条件最大信息数目为:' + max + ')'">
                <el-input-number v-model="exportForm.number" :min="0" :max="max" label="毕业生信息条数"></el-input-number>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" plain @click="exportMsgByCondition">按条件导出</el-button>
                <el-button type="success" plain class="btn_" @click="exportAllMsg">全部导出</el-button>
            </el-form-item>

        </el-form>
    </el-card>

</template>

<script>
import jwtPrase from '@/assets/js/jwtPrase';
export default {
    name: "StudentMsgExportForm",
    data() {
        return {
            exportForm: {
                major: '',
                year: '全部',
                number: 0,
            },
            //数据最大条数
            max: 10,
            majorList: [],
            yearList: []
        }

    },
    methods: {

        //getMax 获取当前选择条件的最大信息数目
        async getMax() {
            let college = jwtPrase().college
            const resp = await this.$http(
                {
                    url: '/userStudent/getExcelMax/' + college,
                    method: "post",
                    data: this.exportForm
                })
            if (resp.data.code === 200) {
                this.max = resp.data.data
            }

        },
        //获取majorList
        async getMajorList() {
            let college = jwtPrase().college
            const resp = await this.$http.get('/dict/major/college/' + college)
            if (resp.data.code === 200) {
                this.majorList = resp.data.data
                this.majorList.unshift({ major: '', description: '全部专业' })
            }
        },
        //获取yearList
        async getYearList() {
            let college = jwtPrase().college
            const resp = await this.$http.get('/userStudent/getYearList/' + college)
            if (resp.data.code === 200) {
                this.yearList = resp.data.data
                this.yearList.unshift('全部')
            }
        },
        //导出所有毕业生数据（学院）
        exportAllMsg() {
            let college = jwtPrase().college

            this.$http({
                method: "get",
                url: "/userStudent/exportAllMsg/" + college,
                responseType: "blob", // 表明返回服务器返回的数据类型
            })
                .then((res) => {
                    // 处理返回的文件流
                    let blob = new Blob([res.data], { type: res.data.type });
                    //获取fileName,截取content-disposition的filename；按=分割，取最后一个
                    const fileName = decodeURI(res.headers['content-disposition'].split("=")[1], "UTF-8");
                    let downloadElement = document.createElement("a");
                    let href = window.URL.createObjectURL(blob); //创建下载的链接
                    downloadElement.href = href;
                    downloadElement.download = fileName; //下载后文件名
                    document.body.appendChild(downloadElement);
                    downloadElement.click(); //点击下载
                    document.body.removeChild(downloadElement); //下载完成移除元素
                    window.URL.revokeObjectURL(href); //释放blob
                    this.$message.success("[毕业生信息]已成功导出!");
                })
                .catch(function (error) {
                    // 请求失败处理
                    console.log(error);
                });
        },
        //按 条件导出 毕业生信息数据
        exportMsgByCondition() {
            let college = jwtPrase().college

            this.$http({
                method: "post",
                data: this.exportForm,
                url: "/userStudent/exportMsgByCondition/" + college,
                responseType: "blob", // 表明返回服务器返回的数据类型
            })
                .then((res) => {
                    // 处理返回的文件流
                    let blob = new Blob([res.data], { type: res.data.type });
                    //获取fileName,截取content-disposition的filename；按=分割，取最后一个
                    const fileName = decodeURI(res.headers['content-disposition'].split("=")[1], "UTF-8");
                    let downloadElement = document.createElement("a");
                    let href = window.URL.createObjectURL(blob); //创建下载的链接
                    downloadElement.href = href;
                    downloadElement.download = fileName; //下载后文件名
                    document.body.appendChild(downloadElement);
                    downloadElement.click(); //点击下载
                    document.body.removeChild(downloadElement); //下载完成移除元素
                    window.URL.revokeObjectURL(href); //释放blob
                    this.$message.success("[毕业生信息]已成功导出!");
                })
                .catch(function (error) {
                    // 请求失败处理
                    console.log(error);
                });
        }

    },
    created() {
        this.getMajorList()
        this.getYearList()
        this.getMax()
    },
    watch: {
        'exportForm.major'() {
            this.getMax()
        },
        'exportForm.year'() {
            this.getMax()
        }
    }

}

</script>

<style scoped>
.box-card2 {
    height: 100px;
}
</style>