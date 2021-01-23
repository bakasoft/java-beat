Panel {
  layout: HORIZONTAL,
  elements: [
    Label {
      text: User
    },
    Panel {
      layout: VERTICAL,
      elements: [
        Label {
          text: Name
        },
        Text {
          value: dummy_value
        },
        Label {
          text: Email
        },
        Text {
          value: dummy_value
        },
        Button {
          text: Save
        }
      ]
    }
  ]
}