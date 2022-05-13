@RestController
class ThisWillActuallyRun {

    @GetMapping("/groovy")
    String home() {
        return "Hello, World!"
    }

}