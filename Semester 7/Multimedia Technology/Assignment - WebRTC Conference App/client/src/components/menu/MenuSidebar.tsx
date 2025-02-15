import { Button, Drawer, Row, Space, Typography } from 'antd';
import Sider from 'antd/es/layout/Sider';
import React, { useState } from 'react';
import AttendeesSection from './AttendeesSection';
import ChatSection from './ChatSection';
import InfoSection from './InfoSection';
import MenuItems from './MenuItems';

enum Category {
  Attendees = "attendees",
  Chat = "chat",
  Video = "video",
  Audio = "audio"
}

const CATEGORY_TITLES: { [key in Category]: string } = {
  [Category.Attendees]: "Attendees",
  [Category.Chat]: "Group Chat",
  [Category.Video]: "Video Settings",
  [Category.Audio]: "Audio Settings",
};

const MenuSidebar: React.FC = () => {
  const [visible, setVisible] = useState(false);
  const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);

  const toggleCategory = (category: Category) => {
    setSelectedCategories((prev) => {
      let updatedCategories;
      if (prev.includes(category)) {
        updatedCategories = prev.filter((item) => item !== category);
      } else {
        updatedCategories = [...prev, category];
      }

      if (updatedCategories.length === 0) {
        setVisible(false);
      } else {
        setVisible(true); 
      }

      return updatedCategories;
    });
  };

  const openDrawer = () => {
    if (selectedCategories.length > 0) {
      setVisible(true);
    }
  };

  const closeDrawer = () => {
    setSelectedCategories([]);
    setVisible(false);
  };

  const closeCategory = (category: Category) => {
    setSelectedCategories((prev) => {
      const updatedCategories = prev.filter((item) => item !== category);

      if (updatedCategories.length === 0) {
        setVisible(false);
      }

      return updatedCategories;
    });
  };

  return (
    <Sider
      width={50}
      className="fixed left-0 top-0 h-full z-50 bg-gray-800"
    >
      <MenuItems
        onClick={toggleCategory}
        selectedCategories={selectedCategories}
        openDrawer={openDrawer}
      />

      <Drawer
        title="Menu"
        placement="left"
        onClose={closeDrawer}
        visible={visible}
        width={280}
        style={{ marginLeft: 50 }}
        mask={false}
        maskClosable={false}
      >
        <div>
          {selectedCategories.map((category) => (
            <div
              key={category}
              className="border-b border-gray-400 p-4"
            >
              <Space direction="vertical" className="w-full">
                <Row justify="space-between" align="middle">
                  <Typography.Title level={5} className="m-0">
                    {CATEGORY_TITLES[category]}
                  </Typography.Title>
                  <Button
                    size="small"
                    shape="circle"
                    onClick={() => closeCategory(category)}
                    icon={<span>x</span>}
                  />
                </Row>

                {category === Category.Attendees && <AttendeesSection />}
                {category === Category.Chat && <ChatSection />}
                {category === Category.Video && <InfoSection />}
                {category === Category.Audio && <InfoSection />}
              </Space>
            </div>
          ))}
        </div>
      </Drawer>
    </Sider>
  );
};

export default MenuSidebar;
