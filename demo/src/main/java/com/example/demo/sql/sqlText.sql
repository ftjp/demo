create table student
(
    s_id    int,
    s_name  varchar(8),
    s_birth date,
    s_sex   varchar(4)
);
insert into student
values (1, '赵雷', '1990-01-01', '男'),
       (2, '钱电', '1990-12-21', '男'),
       (3, '孙风', '1990-05-20', '男'),
       (4, '李云', '1990-08-06', '男'),
       (5, '周梅', '1991-12-01', '女'),
       (6, '吴兰', '1992-03-01', '女'),
       (7, '郑竹', '1989-07-01', '女'),
       (8, '王菊', '1990-01-20', '女');

create table course
(
    c_id   int,
    c_name varchar(8),
    t_id   int
);
insert into course
values (1, '语文', 2),
       (2, '数学', 1),
       (3, '英语', 3);

create table teacher
(
    t_id   int,
    t_name varchar(8)
);
insert into teacher
values (1, '张三'),
       (2, '李四'),
       (3, '王五');

create table score
(
    s_id    int,
    c_id    int,
    s_score int
);
insert into score
values (1, 1, 80),
       (1, 2, 90),
       (1, 3, 99),
       (2, 1, 70),
       (2, 2, 60),
       (2, 3, 65),
       (3, 1, 80),
       (3, 2, 80),
       (3, 3, 80),
       (4, 1, 50),
       (4, 2, 30),
       (4, 3, 40),
       (5, 1, 76),
       (5, 2, 87),
       (6, 1, 31),
       (6, 3, 34),
       (7, 2, 89),
       (7, 3, 98);

-- 1、查询"01"课程比"02"课程成绩高的学生的信息及课程分数
SELECT st.*,
       sc1.s_score "课程1成绩",
       sc2.s_score "课程2成绩"
FROM student st
         LEFT JOIN (SELECT s_id, s_score FROM score WHERE c_id = 1) sc1 ON st.s_id = sc1.s_id
         LEFT JOIN (SELECT s_id, s_score FROM score WHERE c_id = 2) sc2 ON st.s_id = sc2.s_id
WHERE sc1.s_score > sc2.s_score;

-- 2、查询"01"课程比"02"课程成绩低的学生的信息及课程分数
-- 3、查询平均成绩大于等于60分的同学的学生编号和学生姓名和平均成绩
SELECT st.s_id,
       st.s_name,
       sc.avg "平均成绩"
FROM student st
         INNER JOIN (SELECT s_id, AVG(s_score) avg FROM score GROUP BY s_id HAVING avg > 60) sc ON st.s_id = sc.s_id;

-- 4、查询平均成绩小于60分的同学的学生编号和学生姓名和平均成绩(包括有成绩的和无成绩的)
SELECT st.s_id,
       st.s_name,
       (CASE WHEN sc.avg IS NULL THEN 0 ELSE sc.avg END) AS "平均成绩"
FROM student st
         LEFT JOIN (SELECT s_id, AVG(s_score) avg FROM score GROUP BY s_id) sc ON st.s_id = sc.s_id
WHERE (CASE WHEN sc.avg IS NULL THEN 0 ELSE sc.avg END) < 60;

-- 5、查询所有同学的学生编号、学生姓名、选课总数、所有课程的总成绩
SELECT st.s_id,
       st.s_name,
       sc.ccount   选课总数,
       sc.sumscore 总成绩
FROM student st
         LEFT JOIN (SELECT s_id, COUNT(1) ccount, SUM(s_score) sumscore FROM score GROUP BY s_id) sc
                   ON st.s_id = sc.s_id;

-- 6、查询"李"姓老师的数量
-- 7、询学过"张三"老师授课的同学的信息
SELECT student.*
FROM student,
     score,
     course,
     teacher
where teacher.t_name = '张三'
  and student.s_id = score.s_id
  and score.c_id = course.c_id
  and course.t_id = teacher.t_id;

-- 8、查询没学过"张三"老师授课的同学的信息
SELECT *
FROM student
where s_id not in (SELECT s_id
                   FROM score
                   where c_id in (SELECT c_id
                                  FROM course
                                  where t_id in (SELECT t_id
                                                 FROM teacher
                                                 WHERE t_name = '张三')));

-- 9、查询学过编号为"01"并且也学过编号为"02"的课程的同学的信息
SELECT *
FROM student
         LEFT JOIN score on score.s_id = student.s_id
         LEFT JOIN course on score.c_id = course.c_id
WHERE course.c_id in (1, 2)
GROUP BY student.s_id
HAVING count(student.s_id) = 2;

-- 10、查询学过编号为"01"但是没有学过编号为"02"的课程的同学的信息
SELECT *
FROM student
         LEFT JOIN score on score.s_id = student.s_id
         LEFT JOIN course on score.c_id = course.c_id
WHERE course.c_id in (1, 2)
GROUP BY student.s_id
HAVING count(student.s_id) = 1
   and course.c_id = 1;

-- 11、查询没有学全所有课程的同学的信息
SELECT *
FROM student
where s_id not in (SELECT s_id FROM score GROUP BY s_id HAVING count(1) = (SELECT count(*) FROM course))

-- 12、查询至少有一门课与学号为"01"的同学所学相同的同学的信息
SELECT *
FROM student
WHERE student.s_id IN (SELECT score.s_id
                       FROM score
                       WHERE score.c_id IN (SELECT score.c_id FROM score WHERE score.s_id = 1)
                         AND score.s_id != 1);

-- 13、查询和"01"号的同学学习的课程完全相同的其他同学的信息
SELECT *
FROM student
where student.s_id not in (SELECT st.s_id
                           FROM student st
                                    JOIN course c
                                    LEFT JOIN score sc on sc.s_id = st.s_id and sc.c_id = c.c_id
                           where sc.s_id is null)
  and student.s_id != 1;

-- 14、查询没学过"张三"老师讲授的任一门课程的学生姓名
-- 15、查询两门及其以上不及格课程的同学的学号，姓名及其平均成绩
SELECT st.s_id, st.s_name, sc1.`平均成绩`
FROM student st
         LEFT JOIN (SELECT sc.s_id, AVG(sc.s_score) 平均成绩 FROM score sc GROUP BY sc.s_id) sc1 on st.s_id = sc1.s_id
where st.s_id in (SELECT sc2.s_id FROM score sc2 where sc2.s_score < 60 GROUP BY sc2.s_id HAVING COUNT(1) > 1);

-- 16、检索"01"课程分数小于60，按分数降序排列的学生信息
SELECT *
FROM student st,
     score sc
where sc.c_id = 1
  and sc.s_score < 60
  and st.s_id = sc.s_id
ORDER BY sc.s_score DESC;

-- 17、按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩
-- 18、查询各科成绩最高分、最低分和平均分，以如下形式显示：课程ID，课程name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率 – 及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
SELECT sc.c_id,
       c.c_name,
       MAX(sc.s_score),
       MIN(sc.s_score),
       AVG(sc.s_score),
       (COUNT(CASE WHEN sc.s_score >= 60 THEN 1 ELSE NULL END) / COUNT(*))                     及格率,
       (COUNT(CASE WHEN sc.s_score >= 70 and sc.s_score < 80 THEN 1 ELSE NULL END) / COUNT(*)) 中等率,
       (COUNT(CASE WHEN sc.s_score >= 80 and sc.s_score < 90 THEN 1 ELSE NULL END) / COUNT(*)) 优良率,
       (COUNT(CASE WHEN sc.s_score >= 90 THEN 1 ELSE NULL END) / COUNT(*))                     优秀率
FROM score sc
         INNER JOIN course c on sc.c_id = c.c_id
GROUP BY sc.c_id;

-- 19、按各科成绩进行排序，并显示排名
SELECT sc.c_id,
       c.c_name,
       sc.s_id,
       st.s_name,
       sc.s_score,
       ((SELECT count(1) FROM score sc1 where sc1.c_id = sc.c_id and sc1.s_score > sc.s_score) + 1) 排名
FROM score sc
         LEFT JOIN student st on sc.s_id = st.s_id
         LEFT JOIN course c on sc.c_id = c.c_id
GROUP BY sc.c_id, st.s_id
ORDER BY sc.c_id, sc.s_score desc;

-- 20、查询学生的总成绩并进行排名
SELECT st.*,
       ssc.sum                          总成绩,
       ((SELECT count(1)
         FROM ((SELECT sc.s_id, SUM(sc.s_score) sum FROM score sc GROUP BY sc.s_id)) ssc1
         where ssc.sum < ssc1.sum) + 1) 排名
FROM student st
         INNER JOIN (SELECT sc.s_id, SUM(sc.s_score) sum FROM score sc GROUP BY sc.s_id) ssc on ssc.s_id = st.s_id
ORDER BY 总成绩 DESC

-- 21、查询不同老师所教不同课程平均分从高到低显示
-- 22、查询所有课程的成绩第2名到第3名的学生信息及该课程成绩
-- 23、统计各科成绩各分数段人数：课程编号,课程名称,[100-85],[85-70],[70-60],[0-60]及所占百分比
-- 24、查询学生平均成绩及其名次
-- 25、查询各科成绩前三名的记录
-- 26、查询每门课程被选修的学生数
-- 27、查询出只有两门课程的全部学生的学号和姓名
-- 28、查询男生、女生人数
-- 29、查询名字中含有"风"字的学生信息
-- 30、查询同名同性学生名单，并统计同名人数
-- 31、查询1990年出生的学生名单
-- 32、查询每门课程的平均成绩，结果按平均成绩降序排列，平均成绩相同时，按课程编号升序排列
-- 33、查询平均成绩大于等于85的所有学生的学号、姓名和平均成绩
-- 34、查询课程名称为"数学"，且分数低于60的学生姓名和分数
-- 35、查询所有学生的课程及分数情况
-- 36、查询任何一门课程成绩在70分以上的学生姓名、课程名称和分数
-- 37、查询课程不及格的学生
-- 38、查询课程编号为01且课程成绩在80分以上的学生的学号和姓名
-- 39、求每门课程的学生人数
-- 40、查询选修"张三"老师所授课程的学生中，成绩最高的学生信息及其成绩
-- 41、查询不同课程成绩相同的学生的学生编号、课程编号、学生成绩
-- 42、查询每门课程成绩最好的前三名
-- 43、统计每门课程的学生选修人数（超过5人的课程才统计）
-- 44、检索至少选修两门课程的学生学号
-- 45、查询选修了全部课程的学生信息
-- 46、查询各学生的年龄(周岁)
-- 47、查询本周过生日的学生
-- 48、查询下周过生日的学生
-- 49、查询本月过生日的学生
-- 50、查询12月份过生日的学生