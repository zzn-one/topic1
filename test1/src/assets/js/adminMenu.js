const menuList = [
    {
        title: "系统管理",
        children: [
            {
                title: "发布任务",
                path: "/home/admin/taskPublish"
            }
        ]
    },
    {
        title: "用户管理",
        children: [
            {
                title: "学生信息",
                path: "/home/admin/infoStudent"
            },
            {
                title: "导师信息",
                path: "/home/admin/infoTutor"
            },
        ]
    },
    {
        title: "课题管理",
        children: [
            {
                title: "审核课题",
                path: "/home/admin/topicCheck"
            },
            {
                title: "全部课题",
                path: "/home/admin/topicDisplay"
            }
        ]
    },

]

export default menuList