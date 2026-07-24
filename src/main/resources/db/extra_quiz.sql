USE green_guide;

INSERT INTO quiz_question (stem, question_type, options, correct_answer, difficulty, explanation, category_tag, status) VALUES
('玻璃瓶回收前应该怎么做？','SINGLE_CHOICE','[{"key":"A","text":"直接扔进垃圾桶"},{"key":"B","text":"清洗干净并沥干"},{"key":"C","text":"打碎后丢弃"},{"key":"D","text":"和其他垃圾混装"}]','B','BEGINNER','玻璃瓶回收前应清空内容物并清洗干净，这样有助于提高回收效率和防止污染其他回收物。','RECYCLABLE','PUBLISHED'),
('大棒骨属于什么垃圾？','SINGLE_CHOICE','[{"key":"A","text":"厨余垃圾"},{"key":"B","text":"可回收物"},{"key":"C","text":"其他垃圾"},{"key":"D","text":"有害垃圾"}]','C','INTERMEDIATE','大棒骨质地坚硬不易粉碎，属于其他垃圾。只有小型骨骼如鸡骨头、鱼骨头才属于厨余垃圾。','OTHER','PUBLISHED'),
('废旧荧光灯管应该如何处理？','SINGLE_CHOICE','[{"key":"A","text":"扔进可回收物桶"},{"key":"B","text":"砸碎后扔进其他垃圾桶"},{"key":"C","text":"密封包装后投入有害垃圾桶"},{"key":"D","text":"埋入花园土壤"}]','C','BEGINNER','荧光灯管含汞，属于有害垃圾。应轻拿轻放，用原包装或纸包好后投入有害垃圾桶，避免破碎导致汞泄漏。','HARMFUL','PUBLISHED'),
('旧衣服属于哪类垃圾？','SINGLE_CHOICE','[{"key":"A","text":"其他垃圾"},{"key":"B","text":"厨余垃圾"},{"key":"C","text":"有害垃圾"},{"key":"D","text":"可回收物"}]','D','BEGINNER','旧衣物属于可回收物中的纺织品类别，可投入旧衣物回收箱或捐赠给慈善机构。','RECYCLABLE','PUBLISHED'),
('以下哪个不属于厨余垃圾？','SINGLE_CHOICE','[{"key":"A","text":"苹果皮"},{"key":"B","text":"鸡蛋壳"},{"key":"C","text":"榴莲壳"},{"key":"D","text":"菜叶"}]','C','INTERMEDIATE','榴莲壳外壳坚硬不易降解，不属于厨余垃圾，应投入其他垃圾桶。苹果皮、鸡蛋壳和菜叶都属于厨余垃圾。','OTHER','PUBLISHED'),
('用过的创可贴属于什么垃圾？','SINGLE_CHOICE','[{"key":"A","text":"可回收物"},{"key":"B","text":"有害垃圾"},{"key":"C","text":"厨余垃圾"},{"key":"D","text":"其他垃圾"}]','D','BEGINNER','使用过的创可贴属于生活废弃物，无法回收，也不属于有害垃圾，应投入其他垃圾桶。','OTHER','PUBLISHED'),
('水银温度计打碎后应该怎么做？','SINGLE_CHOICE','[{"key":"A","text":"用扫把扫起来扔进可回收物桶"},{"key":"B","text":"打开窗户通风，戴手套收集水银并密封后投入有害垃圾桶"},{"key":"C","text":"直接冲入下水道"},{"key":"D","text":"不管它，自然挥发"}]','B','CHALLENGE','水银（汞）有毒且易挥发，打碎后应立即开窗通风，人员撤离，戴手套小心收集水银密闭包装后投入有害垃圾桶。','HARMFUL','PUBLISHED'),
('咖啡渣属于什么垃圾？','SINGLE_CHOICE','[{"key":"A","text":"可回收物"},{"key":"B","text":"厨余垃圾"},{"key":"C","text":"其他垃圾"},{"key":"D","text":"有害垃圾"}]','B','BEGINNER','咖啡渣属于厨余垃圾，沥干水分后投入厨余垃圾桶，也可以用作植物肥料。','KITCHEN','PUBLISHED');
