import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class StudentMarksAnalysis {

    // Mapper Class
    public static class MarksMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private Text studentName = new Text();
        private IntWritable marks = new IntWritable();

        @Override
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();

            // Skip header
            if (line.startsWith("StudentID")) {
                return;
            }

            String[] data = line.split(",");

            if (data.length == 4) {
                String name = data[1].trim();
                int mark = Integer.parseInt(data[3].trim());

                studentName.set(name);
                marks.set(mark);

                context.write(studentName, marks);
            }
        }
    }

    // Reducer Class
    public static class MarksReducer
            extends Reducer<Text, IntWritable, Text, Text> {

        private Text result = new Text();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                            Context context)
                throws IOException, InterruptedException {

            int total = 0;
            int count = 0;
            int highest = Integer.MIN_VALUE;
            int lowest = Integer.MAX_VALUE;

            for (IntWritable value : values) {

                int mark = value.get();

                total += mark;
                count++;

                if (mark > highest) {
                    highest = mark;
                }

                if (mark < lowest) {
                    lowest = mark;
                }
            }

            double average = (double) total / count;

            String grade;

            if (average >= 90) {
                grade = "A+";
            } else if (average >= 80) {
                grade = "A";
            } else if (average >= 70) {
                grade = "B";
            } else if (average >= 60) {
                grade = "C";
            } else if (average >= 50) {
                grade = "D";
            } else {
                grade = "F";
            }

            String output =
                    "Total=" + total +
                    ", Average=" + String.format("%.2f", average) +
                    ", Highest=" + highest +
                    ", Lowest=" + lowest +
                    ", Grade=" + grade;

            result.set(output);

            context.write(key, result);
        }
    }

    // Driver Class
    public static void main(String[] args)
            throws Exception {

        if (args.length != 2) {
            System.err.println(
                    "Usage: StudentMarksAnalysis <input path> <output path>");
            System.exit(2);
        }

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Student Marks Analysis");

        job.setJarByClass(StudentMarksAnalysis.class);

        job.setMapperClass(MarksMapper.class);
        job.setReducerClass(MarksReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(
                job, new Path(args[0]));

        FileOutputFormat.setOutputPath(
                job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
          }
