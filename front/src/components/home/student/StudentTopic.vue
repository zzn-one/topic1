<template>
    <div>
        <el-descriptions class="margin-top" title="我的毕业设计课题" :column="1" border>
            <template v-for="label, index  in labelList ">
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-s-data"></i>
                        {{ label }}
                    </template>
                    {{ valueList[index] }}
                </el-descriptions-item>
            </template>
        </el-descriptions>
    </div>
</template>

<script>
export default {
    name: "StudentTopic",
    components: {

    },
    data() {
        const LabelList = ["课题名称",
            "课题类型",
            "课题来源",
            "指导老师",
            "指导老师职称",
            "指导老师手机号",
            "指导老师邮箱",
            "指导老师办公室"
        ]

        return {
            labelList: LabelList,
            valueList: []
        }
    },
    created(){
        //根据sId获取课题信息,再根据课题信息中的tId获取导师信息
        this.valueList[0]="高校智能毕业设计选题管理系统"
        this.valueList[1]="设计"
        this.valueList[2]="自命题"

        //导师信息
        this.valueList[3]="晨钟"
        this.valueList[4]="教授"
        this.valueList[5]="1664826408"
        this.valueList[6]="chenz@163.com"
        this.valueList[7]="xxx楼701"

    }

}
</script>

<style scoped lang="less">
/deep/.el-descriptions__header{
    line-height: 100px;
}
.main {
    background-color: rgba(255, 255, 255, 0.7);
}

/deep/.el-descriptions__body .el-descriptions__table {
    height: 700px;

}

/deep/.el-descriptions__body {
    background-color: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
}

/deep/.el-descriptions {
    font-size: 18px;
}

/deep/.el-descriptions__title {
    font-size: 24px;
}

/deep/.el-descriptions-item__label.is-bordered-label {
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    color: black;
}
</style>