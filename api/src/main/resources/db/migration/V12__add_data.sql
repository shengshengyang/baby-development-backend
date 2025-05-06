-- 插入 Milestones 數據
INSERT INTO milestones (id, age_in_months, category) VALUES
                                                         (9, 4, '綜合發展'),
                                                         (10, 6, '綜合發展'),
                                                         (11, 9, '綜合發展');

-- 插入 Flashcards 數據
INSERT INTO flashcards (id, category, milestone_id) VALUES
-- 4 個月
(22, '粗大動作', 9),
(23, '粗大動作', 9),
(24, '精細動作', 9),
(25, '精細動作', 9),
(26, '語言', 9),
(27, '語言', 9),
(28, '社交', 9),
(29, '社交', 9),
-- 6 個月
(30, '粗大動作', 10),
(31, '粗大動作', 10),
(32, '精細動作', 10),
(33, '精細動作', 10),
(34, '語言', 10),
(35, '語言', 10),
(36, '社交', 10),
(37, '社交', 10),
-- 9 個月
(38, '粗大動作', 11),
(39, '粗大動作', 11),
(40, '精細動作', 11),
(41, '精細動作', 11),
(42, '語言', 11),
(43, '語言', 11),
(44, '社交', 11),
(45, '社交', 11);

-- 插入 Flashcard Translations 數據
INSERT INTO flashcard_translations (id, flashcard_id, language_code, front_text, back_text, image_url) VALUES
-- 4 個月
(22, 22, 'en', 'Lifts head when lying on back', 'Observe if the baby can lift their head briefly when placed on their back.', ''),
(23, 22, 'zh', '仰臥時抬頭', '觀察寶寶仰臥時是否能短暫抬起頭部。', ''),
(24, 23, 'en', 'Props up on forearms when lying on stomach', 'Check if the baby can use their forearms to lift their chest off the ground when on their stomach.', ''),
(25, 23, 'zh', '俯臥時用前臂撐起', '檢查寶寶俯臥時是否能用前臂撐起胸部離地。', ''),
(26, 24, 'en', 'Grasps toy', 'See if the baby can hold a toy placed in their hand.', ''),
(27, 24, 'zh', '抓握玩具', '觀察寶寶是否能握住放在手中的玩具。', ''),
(28, 25, 'en', 'Brings hand to mouth', 'Watch if the baby can bring their hand to their mouth intentionally.', ''),
(29, 25, 'zh', '將手移到嘴邊', '觀察寶寶是否能有意識地將手移到嘴邊。', ''),
(30, 26, 'en', 'Makes sounds', 'Listen if the baby can produce cooing or babbling sounds.', ''),
(31, 26, 'zh', '發出聲音', '聽寶寶是否能發出咕咕或咿咿呀呀的聲音。', ''),
(32, 27, 'en', 'Responds to sounds', 'Check if the baby turns their head or reacts to loud noises.', ''),
(33, 27, 'zh', '對聲音有反應', '觀察寶寶是否會轉頭或對大聲音有反應。', ''),
(34, 28, 'en', 'Smiles', 'See if the baby smiles in response to your face or voice.', ''),
(35, 28, 'zh', '微笑', '觀察寶寶是否會對你的臉或聲音微笑。', ''),
(36, 29, 'en', 'Watches faces', 'Observe if the baby can focus on and follow faces with their eyes.', ''),
(37, 29, 'zh', '注視人臉', '觀察寶寶是否能專注並用眼睛跟隨人臉。', ''),
-- 6 個月
(38, 30, 'en', 'Rolls over', 'Watch if the baby can roll from their back to their stomach and vice versa.', ''),
(39, 30, 'zh', '翻身', '觀察寶寶是否能從背部翻到腹部並反之。', ''),
(40, 31, 'en', 'Sits with support', 'Check if the baby can sit steadily with some support.', ''),
(41, 31, 'zh', '坐穩（有支撐）', '觀察寶寶在有支撐的情況下是否能坐穩。', ''),
(42, 32, 'en', 'Grasps small objects', 'See if the baby can pick up small items using their whole hand.', ''),
(43, 32, 'zh', '抓握小物件', '觀察寶寶是否能用整隻手拿起小物品。', ''),
(44, 33, 'en', 'Transfers objects from one hand to the other', 'Watch if the baby can pass a toy from one hand to the other.', ''),
(45, 33, 'zh', '將物品從一手傳到另一手', '觀察寶寶是否能將玩具從一手傳到另一手。', ''),
(46, 34, 'en', 'Makes various sounds', 'Listen if the baby can produce different types of babbling sounds.', ''),
(47, 34, 'zh', '發出多種聲音', '聽寶寶是否能發出不同類型的咿咿呀呀聲。', ''),
(48, 35, 'en', 'Imitates sounds', 'See if the baby tries to mimic sounds you make.', ''),
(49, 35, 'zh', '模仿聲音', '觀察寶寶是否嘗試模仿你發出的聲音。', ''),
(50, 36, 'en', 'Recognizes familiar people', 'Check if the baby shows recognition of family members or caregivers.', ''),
(51, 36, 'zh', '認得熟悉的人', '觀察寶寶是否能認出家人或照顧者。', ''),
(52, 37, 'en', 'Responds to mirror', 'See if the baby reacts to their reflection in a mirror.', ''),
(53, 37, 'zh', '對鏡子有反應', '觀察寶寶是否對鏡子中的自己有反應。', ''),
-- 9 個月
(54, 38, 'en', 'Crawls', 'Watch if the baby can move forward on their hands and knees.', ''),
(55, 38, 'zh', '爬行', '觀察寶寶是否能用手和膝蓋向前移動。', ''),
(56, 39, 'en', 'Pulls to stand', 'See if the baby can pull themselves up to a standing position using furniture.', ''),
(57, 39, 'zh', '拉著站立', '觀察寶寶是否能拉著家具站起來。', ''),
(58, 40, 'en', 'Picks up small objects with thumb and index finger', 'Check if the baby can use a pincer grasp to pick up tiny items.', ''),
(59, 40, 'zh', '用拇指和食指抓握小物件', '觀察寶寶是否能用拇指和食指夾取小物品。', ''),
(60, 41, 'en', 'Bangs toys together', 'See if the baby can hold a toy in each hand and bang them together.', ''),
(61, 41, 'zh', '敲打玩具', '觀察寶寶是否能一手拿一個玩具並敲打在一起。', ''),
(62, 42, 'en', 'Says simple words', 'Listen if the baby can say words like "mama" or "dada".', ''),
(63, 42, 'zh', '說出簡單詞彙', '聽寶寶是否能說出“媽媽”或“爸爸”等詞。', ''),
(64, 43, 'en', 'Understands simple commands', 'Check if the baby can follow basic instructions like "come here".', ''),
(65, 43, 'zh', '理解簡單指令', '觀察寶寶是否能聽懂“過來”等簡單指令。', ''),
(66, 44, 'en', 'Imitates actions', 'See if the baby can copy simple gestures like clapping hands.', ''),
(67, 44, 'zh', '模仿動作', '觀察寶寶是否能模仿拍手等簡單動作。', ''),
(68, 45, 'en', 'Interacts with others', 'Watch if the baby engages in games like peek-a-boo or responds to their name.', ''),
(69, 45, 'zh', '與人互動', '觀察寶寶是否參與躲貓貓等遊戲或對自己的名字有反應。', '');
