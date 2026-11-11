public class ShapeHierarchy {
    // 1. Create an abstract Shape class with abstract double area()
    public static abstract class Shape {
        public abstract double area();
        protected abstract String getShapeName();

        // 3. Add getShape() that returns a comma-separated string showing the type and area
        public String getShape() {
            // Was going to just use this.getClass().getSimpleName()
            // Buuuuuuut for exercise purposes, let's use some more overriden methods
            return String.format("%s,%.2f", this.getShapeName(), this.area());
        }
    }

    // 2. Extend it with Circle, Rectangle, and Square extends Rectangle
    public static class Circle extends Shape {
        private double radius;
        public Circle(double radius) {
            this.radius = radius;
        }

        @Override
        public double area() {
            // piR^2
            return Math.PI * Math.pow(this.radius, 2);
        }

        @Override 
        protected String getShapeName() {
            return "Circle";
        }
    }

    public static class Rectangle extends Shape {
        private double width;
        private double height;
        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public double area() {
            return width * height;
        }

        @Override 
        protected String getShapeName() {
            return "Rectangle";
        }
    }

    public static class Square extends Rectangle {
        public Square(double length) {
            super(length, length);
        }

        @Override 
        protected String getShapeName() {
            return "Square";
        }
    }

    public static void main(String[] args) {
        // 4. Store a mix of shapes in a List<Shape> or Shape[]
        Shape[] shapes = new Shape[] {
            new Square(5),
            new Rectangle(3, 4),
            new Circle(4)
        };
        // Stretch: track the largest shape in the same loop and print it at the end
        double largestArea = 0.0;
        Shape largestShape = null;
        // 5. Print every area in one loop - no instanceof
        for (Shape shape : shapes) {
            System.out.println(shape.getShape().replace(",", ", "));
            if(shape.area() > largestArea) {
                largestArea = shape.area();
                largestShape = shape;
            }
        }
        System.out.println("Largest Shape: " + largestShape.getShape().replace(",", ", "));
    }
}
