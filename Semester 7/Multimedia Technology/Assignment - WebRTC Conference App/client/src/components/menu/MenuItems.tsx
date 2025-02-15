import { CommentOutlined, EllipsisOutlined, InfoCircleOutlined, UsergroupAddOutlined } from '@ant-design/icons';
import { Menu } from 'antd';
import React from 'react';

enum Category {
  Attendees = "attendees",
  Chat = "chat",
  Video = "video",
  Audio = "audio"
}

interface MenuItemsProps {
  onClick: (category: Category) => void;
  selectedCategories: Category[];
  openDrawer: () => void;
}

const MenuItems: React.FC<MenuItemsProps> = ({ onClick, selectedCategories, openDrawer }) => {
  const categories = [
    { key: Category.Attendees, label: 'Attendees', icon: <UsergroupAddOutlined /> },
    { key: Category.Chat, label: 'Group Chat', icon: <CommentOutlined /> },
    { key: Category.Video, label: 'Video Settings', icon: <InfoCircleOutlined /> },
    { key: Category.Audio, label: 'Audio Settings', icon: <EllipsisOutlined /> }
  ];

  return (
    <Menu
      mode="inline"
      theme="dark"
      style={{ height: '100%' }}
    >
      {categories.map(({ key, label, icon }) => (
        <Menu.Item
          key={key}
          icon={icon}
          style={{
            display: 'flex',
            justifyContent: 'center',
            color: 'white',
            backgroundColor: selectedCategories.includes(key) ? '#1890ff' : 'transparent',
          }}
          onClick={() => {
            openDrawer();
            onClick(key);
          }}
        >
          {label}
        </Menu.Item>
      ))}
    </Menu>
  );
};

export default MenuItems;
