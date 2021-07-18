Blockly.defineBlocksWithJsonArray([
  {
    type: "geometric_reflectimage",
    message0: "Reflect image in %1 direction",
    args0: [
      {
        type: "field_dropdown",
        name: "direction_dropdown",
        options: [
          ["X", "X"],
          ["Y", "Y"],
          ["Both", "Both"],
        ],
      },
    ],
    previousStatement: null,
    nextStatement: null,
    colour: 20,
    tooltip:
      "Refelction produces a missror image like about and an axis of reflection. There are two type of reflection. One is x direction reflection and other is y direction reflection",
    helpUrl: "",
  },
  {
    type: "geometric_rotateimage",
    message0: "Rotate image with angle of %1 and rescale by %2",
    args0: [
      {
        type: "field_angle",
        name: "rotation_angle",
        angle: 90,
      },
      {
        type: "field_input",
        name: "rotation_scale",
        text: "1",
      },
    ],
    previousStatement: null,
    nextStatement: null,
    colour: 20,
    tooltip:
      "This operator allows you to rotate an image to a specific angle. Moreover you can change angle and scale related to the rotation.",
    helpUrl: "",
  },
  {
    type: "geometric_colormaps",
    message0: "Color map image with %1 filter",
    args0: [
      {
        type: "field_dropdown",
        name: "colormap_dropdown",
        options: [
          ["Hot", "HOT"],
          ["Autumn", "AUTUMN"],
          ["Bone", "BONE"],
          ["Cool", "COOL"],
          ["HSV", "HSV"],
          ["JET", "JET"],
          ["Ocean", "OCEAN"],
          ["Parula", "PARULA"],
          ["Piink", "PINK"],
          ["Rainbow", "RAINBOW"],
        ],
      },
    ],
    previousStatement: null,
    nextStatement: null,
    colour: 20,
    tooltip:
      "Color maps can apply different color maps to an image using this method. OpenCV caters various other types All these types are represents by predefined static fields(fixed values).",
    helpUrl: "",
  },
  {
    type: "geometric_affineimage",
    message0: "Affine Image",
    previousStatement: null,
    nextStatement: null,
    colour: 20,
    tooltip:
      "Image Affine Translation or shearing express in a materix form. It's a combination of shearing and reflection",
    helpUrl: "",
  },
  {
    type: "geometric_scaleimage",
    message0: "Scale Image by %1 in X axis and by %2 in Y axis",
    args0: [
      {
        type: "field_input",
        name: "scale_x",
        text: "1",
      },
      {
        type: "field_input",
        name: "scale_y",
        text: "1",
      },
    ],
    previousStatement: null,
    nextStatement: null,
    colour: 20,
    tooltip:
      "Image Affine Translation or shearing express in a materix form. It's a combination of shearing and reflection",
    helpUrl: "",
  },
]);
