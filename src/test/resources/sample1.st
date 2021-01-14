Panel<1> {
  layout: HORIZONTAL,
  elements: [
    Label<1> {
      text: User
    },
    Panel<2> {
      layout: VERTICAL,
      elements: [
        Label<2> {
          text: Name
        },
        Text<1> {
          value: dummy_value
        },
        Label<3> {
          text: Email
        },
        Text<2> {
          value: dummy_value
        },
        Button<1> {
          text: Save
        }
      ]
    }
  ]
}